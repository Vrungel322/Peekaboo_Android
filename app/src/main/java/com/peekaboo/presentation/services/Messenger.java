package com.peekaboo.presentation.services;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.peekaboo.data.FileEntity;
import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.data.repositories.database.messages.PMessageAbs;
import com.peekaboo.data.repositories.database.messages.PMessageHelper;
import com.peekaboo.data.repositories.database.service.ReadMessagesHelper;
import com.peekaboo.data.repositories.database.service.ServiceMessageAbs;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.domain.Pair;
import com.peekaboo.domain.subscribers.BaseUseCaseSubscriber;
import com.peekaboo.domain.usecase.FileDownloadUseCase;
import com.peekaboo.domain.usecase.FileUploadUseCase;
import com.peekaboo.presentation.fragments.ChatFragment;
import com.peekaboo.utils.Constants;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

/**
 * Created by sebastian on 22.08.16.
 */
public class Messenger implements IMessenger,
        INotifier.NotificationListener<Message> {
    private final INotifier<Message> notifier;
    private final PMessageHelper helper;
    private final ReadMessagesHelper readMessagesHelper;
    Subscription unreadMessages = Subscriptions.empty();
    Subscription undeliveredMessages = Subscriptions.empty();
    Subscription serviceMessages = Subscriptions.empty();
    private AccountUser user;
    private FileUploadUseCase uploadFileUseCase;
    private FileDownloadUseCase downloadFileUseCase;
    private Set<MessengerListener> messageListeners = new HashSet<>();
    private MessageNotificator messageNotificator;

    @Nullable
    private ChatFragment.DISABLE_pbLoadingImageToServer pbLoadingImageToServerDisableListener;

    public Messenger(INotifier<Message> notifier, PMessageHelper helper,
                     MessageNotificator messageNotificator,
                     ReadMessagesHelper readMessagesHelper, AccountUser user,
                     FileUploadUseCase uploadFileUseCase, FileDownloadUseCase downloadFileUseCase) {
        this.notifier = notifier;
        this.helper = helper;
        this.messageNotificator = messageNotificator;
        this.readMessagesHelper = readMessagesHelper;
        this.user = user;
        this.uploadFileUseCase = uploadFileUseCase;
        this.downloadFileUseCase = downloadFileUseCase;
        notifier.addListener(this);
    }

    @Override
    public Observable<List<PMessage>> getAllMessages(String tableName) {
        return helper.getAllMessages(tableName);
    }

    @Override
    public Observable<List<PMessage>> getUnreadMessages(String tableName) {
        return helper.getUnreadMessages(tableName, false);
    }

    @Override
    public void addMessageListener(MessengerListener messengerListener) {
        messageListeners.add(messengerListener);
    }

    @Override
    public void removeMessageListener(MessengerListener messengerListener) {
        messageListeners.remove(messengerListener);
    }

    @Override
    public void onMessageObtained(Message message) {
        Log.e("messanger", "obtained " + message);
        switch (message.getCommand()) {
            case MESSAGE:
                handleIncomingMessage(message);
                break;
            case SYSTEMMESSAGE:
                if (Message.Reason.READ.equals(message.getParams().get(Message.Params.REASON))) {
                    handleIncomingReadNotification(message);
                }
                break;
        }
    }

    /**
     * Called when READ_NOTIFICATION message was obtained.
     * Calls {@link #updateMessageRead(PMessage, String)} for of all outcoming messages with DELIVERED status
     * for certain user
     *
     * @param message READ notification message
     */
    private void handleIncomingReadNotification(Message message) {
        if (unreadMessages != null && !unreadMessages.isUnsubscribed()) {
            unreadMessages.unsubscribe();
        }
        final String tableName = message.getParams().get(Message.Params.FROM);
        unreadMessages = helper.getUnreadMessages(tableName, true)
                .subscribe(new BaseUseCaseSubscriber<List<PMessage>>() {
                    @Override
                    public void onNext(List<PMessage> pMessages) {
                        for (PMessage pMessage : pMessages) {
                            updateMessageRead(pMessage, tableName);
                        }
                    }
                });
    }

    /**
     * Called when new message was obtained.
     * Adds incoming message to database with status DELIVERED.
     * Changes status of incoming message on READ if at least one of MessengerListeners return status READ.
     * In such situation {@link #readMessage(PMessage)} will be called
     *
     * @param message
     */
    private void handleIncomingMessage(Message message) {
        PMessage pMessage = MessageUtils.convert(message);
        Log.e("Messenger", "type " + pMessage.mediaType());
        pMessage.setStatus(PMessage.PMESSAGE_STATUS.STATUS_DELIVERED);
        String tableName = pMessage.senderId();
        helper.insert(tableName, pMessage);

        boolean isRead = false;
        boolean isIgnored = true;
        for (MessengerListener listener : messageListeners) {
            int status = listener.displayStatus(pMessage);
            if (status != MessengerListener.STATUS_IGNORE) {
                isIgnored = false;
                if (status == PMessageAbs.PMESSAGE_STATUS.STATUS_READ) {
                    isRead = true;
                    break;
                }
            }
        }

        if (isRead) {
            readMessage(pMessage);
        } else {
            if (isIgnored) {
                Log.e("messenger", "" + message);
                messageNotificator.onMessageObtained(pMessage);
            }
            for (MessengerListener listener : messageListeners) {
                listener.onMessageUpdated(pMessage);
            }
        }
        Log.e("Messenger", "type " + pMessage.mediaType());
        if (pMessage.mediaType() == PMessage.PMESSAGE_MEDIA_TYPE.AUDIO_MESSAGE) {
            Log.e("Messenger", "download begin");
            downloadFileUseCase.execute(pMessage, getDownloadSubscriber(), Constants.MESSAGE_TYPE.TYPE_AUDIO);
        }

        if (pMessage.mediaType() == PMessage.PMESSAGE_MEDIA_TYPE.IMAGE_MESSAGE) {
            Log.e("Messenger", "download begin");
            downloadFileUseCase.execute(pMessage, getDownloadSubscriber(), Constants.MESSAGE_TYPE.TYPE_IMAGE);
        }

    }

    /**
     * Called for incoming delivered messages, which should became read.
     * Calls {@link #updateMessageRead(PMessage, String)} for incoming message.
     * If socket is available, calls {@link #deliverReadServiceMessage(String)} for author of incoming message,
     * else adds new {@link ServiceMessageAbs}
     *
     * @param message incoming message
     */
    @Override
    public void readMessage(PMessage message) {
        if (!message.isMine()) {
            String senderId = message.senderId();

            updateMessageRead(message, senderId);

            if (isAvailable()) {
                deliverReadServiceMessage(senderId);
            } else {
                readMessagesHelper.insert(senderId);
            }

        }
    }

    /**
     * changes status of any message on READ, notifies listeners
     *
     * @param message   message, which status will be changed
     * @param tableName
     */
    private void updateMessageRead(PMessage message, String tableName) {
        helper.updateStatus(tableName, PMessageAbs.PMESSAGE_STATUS.STATUS_READ, message);

        for (MessengerListener listener : messageListeners) {
            listener.onMessageUpdated(message);
        }
    }

    /**
     * adds outcoming message to database with status SENT.
     * If socket is available {@link #deliverMessage(PMessage)} will be called.
     *
     * @param message outcoming message
     */
    @Override
    public void sendMessage(PMessage message) {
        message.setStatus(PMessageAbs.PMESSAGE_STATUS.STATUS_SENT);
        helper.insert(message.receiverId(), message);
        for (MessengerListener listener : messageListeners) {
            listener.onMessageUpdated(message);
        }
        deliverMessageByMediatype(message);
    }

    private void deliverMessageByMediatype(PMessage message) {
        switch (message.mediaType()) {
            case PMessage.PMESSAGE_MEDIA_TYPE.TEXT_MESSAGE:
            case PMessage.PMESSAGE_MEDIA_TYPE.GEO_MESSAGE:
                if (isAvailable()) {
                    deliverMessage(message);
                }
                break;
            case PMessage.PMESSAGE_MEDIA_TYPE.AUDIO_MESSAGE:
            case PMessage.PMESSAGE_MEDIA_TYPE.IMAGE_MESSAGE:
                uploadAndDeliverFileMessage(message);
                break;
        }
    }

    private void uploadAndDeliverFileMessage(PMessage message) {
        if (message.mediaType() == PMessageAbs.PMESSAGE_MEDIA_TYPE.AUDIO_MESSAGE) {
            uploadFileUseCase.execute(message, getUploadSubscriber(), Constants.MESSAGE_TYPE.TYPE_AUDIO);
        }

        if (message.mediaType() == PMessageAbs.PMESSAGE_MEDIA_TYPE.IMAGE_MESSAGE) {
            Log.wtf("gus :", "uploadAndDeliverFileMessage -> IMAGE_MESSAGE");
            uploadFileUseCase.execute(message, getUploadSubscriber(), Constants.MESSAGE_TYPE.TYPE_IMAGE);
        }
    }

    @NonNull
    private BaseUseCaseSubscriber<Pair<PMessage, String>> getDownloadSubscriber() {
        return new BaseUseCaseSubscriber<Pair<PMessage, String>>() {
            @Override
            public void onNext(Pair<PMessage, String> pair) {
                super.onNext(pair);
                PMessage first = pair.first;
                String local = pair.second;
                String remote = first.messageBody();
                String audioBody = MessageUtils.createAudioBody(remote, local, local == null);
                helper.updateBody(first.senderId(), first, audioBody);
                for (MessengerListener listener : messageListeners) {
                    listener.onMessageUpdated(first);
                }
            }
        };
    }

    @NonNull
    private BaseUseCaseSubscriber<Pair<PMessage, FileEntity>> getUploadSubscriber() {
        return new BaseUseCaseSubscriber<Pair<PMessage, FileEntity>>() {
            @Override
            public void onNext(Pair<PMessage, FileEntity> pMessageFileEntityPair) {
                if (isAvailable() && pMessageFileEntityPair.second != null) {
                    Log.wtf("getUploadSubscriber : ", "upload end");
                    PMessage pMessage = pMessageFileEntityPair.first;
                    if (pMessage.mediaType() == PMessageAbs.PMESSAGE_MEDIA_TYPE.IMAGE_MESSAGE & pbLoadingImageToServerDisableListener != null) {
                        pbLoadingImageToServerDisableListener.disablePbLoadingImageToServer();
                    }
                    FileEntity fileEntity = pMessageFileEntityPair.second;
                    String remote = fileEntity.getName();
                    String local = pMessage.messageBody();
                    String audioBody = MessageUtils.createAudioBody(remote, local, false);
                    helper.updateBody(pMessage.receiverId(), pMessage, audioBody);
                    Log.e("messenger", "upload " + pMessage);
                    deliverMessage(pMessage);
                }
            }
        };
    }

    /**
     * Sends outcoming message by socket. changes status of outcoming message to status DELIVERED.
     * Notifies MessengerListeners
     *
     * @param message
     */
    private void deliverMessage(PMessage message) {
        notifier.sendMessage(MessageUtils.convert(message));
        helper.updateStatus(message.receiverId(), PMessageAbs.PMESSAGE_STATUS.STATUS_DELIVERED, message);
        for (MessengerListener listener : messageListeners) {
            listener.onMessageUpdated(message);
        }
    }

    @Override
    public void setpbLoadingImageToServerDisableListener(ChatFragment.DISABLE_pbLoadingImageToServer pbLoadingImageToServerDisableListener) {
        this.pbLoadingImageToServerDisableListener = pbLoadingImageToServerDisableListener;
    }

    @Override
    public boolean isAvailable() {
        return notifier.isAvailable();
    }

    @Override
    public void tryConnect(String authorization) {
        Log.e("Messenger", "try connect " + hashCode());
        notifier.tryConnect(authorization);
    }

    @Override
    public void addListener(NotificationListener<PMessage> listener) {
    }

    @Override
    public void removeListener(NotificationListener<PMessage> listener) {
    }

    @Override
    public void disconnect() {
        Log.e("Messenger", "disconnect " + hashCode());
        notifier.disconnect();
    }

    @Override
    public void onConnected() {
        Log.e("Messenger", "connected " + hashCode());
        deliverSentMessages();
        deliverReadServiceMessages();
    }

    /**
     * Sends READ_NOTIFICATION messages by socket for messages,
     * which were read while socket was not available.
     */
    private void deliverReadServiceMessages() {
        if (serviceMessages != null && !serviceMessages.isUnsubscribed()) {
            serviceMessages.unsubscribe();
        }
        serviceMessages = readMessagesHelper.getUnreadMessages()
                .subscribe(new BaseUseCaseSubscriber<List<String>>() {
                    @Override
                    public void onNext(List<String> senders) {
                        for (String sender : senders) {
                            if (isAvailable()) {
                                deliverReadServiceMessage(sender);
                                readMessagesHelper.delete(sender);
                            }
                        }
                    }

                });
    }

    /**
     * Delivers messages which were sent while socket was not available.
     * {@link #deliverMessage(PMessage)} will be called for each undelivered message
     */
    private void deliverSentMessages() {
        if (undeliveredMessages != null && !undeliveredMessages.isUnsubscribed()) {
            undeliveredMessages.unsubscribe();
        }
        undeliveredMessages = helper.getUndeliveredMessages()
                .subscribe(new BaseUseCaseSubscriber<List<PMessage>>() {
                    @Override
                    public void onNext(List<PMessage> pMessages) {
                        for (PMessage message : pMessages) {
                            deliverMessageByMediatype(message);
                        }
                    }
                });
    }

    /**
     * send READ_NOTIFICATION by socket for message sender
     *
     * @param senderId id of sender
     */
    private void deliverReadServiceMessage(String senderId) {
        Message message = MessageUtils.createReadMessage(senderId, user.getId());
        notifier.sendMessage(message);
    }

    @Override
    public void onDisconnected() {

    }
}
