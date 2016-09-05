package com.peekaboo.presentation.services;

import android.content.ContentValues;
import android.util.Log;

import com.peekaboo.data.mappers.AbstractMapperFactory;
import com.peekaboo.data.mappers.Mapper;
import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.data.repositories.database.messages.PMessageAbs;
import com.peekaboo.data.repositories.database.messages.PMessageHelper;
import com.peekaboo.data.repositories.database.service.ReadMessagesHelper;
import com.peekaboo.data.repositories.database.service.ServiceMessageAbs;
import com.peekaboo.domain.AccountUser;

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
    private final Mapper<PMessageAbs, ContentValues> pMessageMapper;
    Subscription unreadMessages = Subscriptions.empty();
    Subscription undeliveredMessages = Subscriptions.empty();
    Subscription serviceMessages = Subscriptions.empty();
    private AccountUser user;
    private Set<MessengerListener> listeners = new HashSet<>();

    public Messenger(INotifier<Message> notifier, PMessageHelper helper, ReadMessagesHelper readMessagesHelper, AccountUser user,
                     AbstractMapperFactory abstractMapperFactory) {
        this.notifier = notifier;
        this.helper = helper;
        this.readMessagesHelper = readMessagesHelper;
        this.user = user;
        pMessageMapper = abstractMapperFactory.getPMessageMapper();
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
        listeners.add(messengerListener);
    }

    @Override
    public void removeMessageListener(MessengerListener messengerListener) {
        listeners.remove(messengerListener);
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
        String tableName1 = message.getParams().get(Message.Params.FROM);
        unreadMessages = helper.getUnreadMessages(tableName1, true).subscribe(pMessages -> {
            unreadMessages.unsubscribe();
            for (PMessage pMessage1 : pMessages) {
                updateMessageRead(pMessage1, tableName1);
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
        PMessage pMessage = MessageUtils.convert(user.getId(), message);
        pMessage.setStatus(PMessage.PMESSAGE_STATUS.STATUS_DELIVERED);
        String tableName = pMessage.senderId();
        helper.insert(tableName, pMessage);

        boolean isRead = false;
        for (MessengerListener listener : listeners) {
            int status = listener.willChangeStatus(pMessage);
            if (status == PMessageAbs.PMESSAGE_STATUS.STATUS_READ) {
                isRead = true;
                break;
            }
        }

        if (isRead) {
            readMessage(pMessage);
        } else {
            for (MessengerListener listener : listeners) {
                listener.onMessageDelivered(pMessage);
            }
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

        for (MessengerListener listener : listeners) {
            listener.onMessageRead(message);
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
        for (MessengerListener listener : listeners) {
            listener.onMessageSent(message);
        }
        if (isAvailable()) {
            deliverMessage(message);
        }
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
        for (MessengerListener listener : listeners) {
            listener.onMessageDelivered(message);
        }
    }

    @Override
    public boolean isAvailable() {
        return notifier.isAvailable();
    }

    @Override
    public void tryConnect(String authorization) {
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
        notifier.disconnect();
    }

    @Override
    public void onConnected() {
        deliverSentMessages();
        deliverReadServiceMessages();
    }

    /**
     * Sends READ_NOTIFICATION messages by socket for messages,
     * which were read while socket was not available.
     */
    private void deliverReadServiceMessages() {
        serviceMessages = readMessagesHelper.getUnreadMessages()
                .subscribe(senders -> {
                    serviceMessages.unsubscribe();
                    Log.e("senders", senders.toString());
                    for (String sender : senders) {
                        if (isAvailable()) {
                            deliverReadServiceMessage(sender);
                            readMessagesHelper.delete(sender);
                        }
                    }
                });
    }

    /**
     * Delivers messages which were sent while socket was not available.
     * {@link #deliverMessage(PMessage)} will be called for each undelivered message
     */
    private void deliverSentMessages() {
        Log.e("BUG1", "deliver");
        undeliveredMessages = helper.getUndeliveredMessages()
                .subscribe(pMessageAbses -> {
                    Log.e("BUG1", "unsubscribe");
                    undeliveredMessages.unsubscribe();
                    for (PMessage message : pMessageAbses) {
                        if (isAvailable()) {
                            deliverMessage(message);
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
