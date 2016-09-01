package com.peekaboo.presentation.services;

import android.content.ContentValues;
import android.util.Log;

import com.peekaboo.data.mappers.AbstractMapperFactory;
import com.peekaboo.data.mappers.Mapper;
import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.data.repositories.database.messages.PMessageAbs;
import com.peekaboo.data.repositories.database.messages.PMessageHelper;
import com.peekaboo.data.repositories.database.service.ServiceMessageAbs;
import com.peekaboo.data.repositories.database.service.ServiceMessagesHelper;
import com.peekaboo.domain.AccountUser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by sebastian on 22.08.16.
 */
public class Messenger implements IMessenger,
        INotifier.NotificationListener<Message> {
    private final INotifier<Message> notifier;
    private final PMessageHelper helper;
    private final ServiceMessagesHelper serviceMessagesHelper;
    private final Mapper<PMessageAbs, ContentValues> pMessageMapper;
    private AccountUser user;

    private Set<MessengerListener> listeners = new HashSet<>();

    public Messenger(INotifier<Message> notifier, PMessageHelper helper, ServiceMessagesHelper serviceMessagesHelper, AccountUser user,
                     AbstractMapperFactory abstractMapperFactory) {
        this.notifier = notifier;
        this.helper = helper;
        this.serviceMessagesHelper = serviceMessagesHelper;
        this.user = user;
        pMessageMapper = abstractMapperFactory.getPMessageMapper();
        notifier.addListener(this);
    }

    @Override
    public void readMessage(PMessage message) {
        if (!message.isMine()) {
            Log.e("messanger", "read " + message);
            String senderId = message.senderId();

            updateMessageRead(message, senderId);

            if (isAvailable()) {
                deliverReadServiceMessage(message.packageId(), senderId);
            } else {
                serviceMessagesHelper.insert(message.packageId(), senderId);
            }

        }
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
        Log.e("messanger", "obtained");
        switch (message.getCommand()) {
            case MESSAGE:
                PMessage pMessage = MessageUtils.convert(user.getId(), message);
                pMessage.setStatus(PMessage.PMESSAGE_STATUS.STATUS_DELIVERED);
                String tableName = pMessage.senderId();
                helper.insert(tableName, pMessageMapper.transform(pMessage));

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
                break;
            case READ:
                String tableName1 = message.getParams().get(Message.Params.FROM);
                helper.getUnreadMessages(tableName1, true).subscribe(new Action1<List<PMessage>>() {
                    @Override
                    public void call(List<PMessage> pMessages) {
                        for (PMessage pMessage1 : pMessages) {
                            updateMessageRead(pMessage1, tableName1);
                        }
                    }
                });
                break;
        }
    }

    private void updateMessageRead(PMessage message, String tableName) {
        int statusRead = PMessageAbs.PMESSAGE_STATUS.STATUS_READ;
        message.setStatus(statusRead);
        helper.updateStatusByPackageId(tableName, statusRead, message.packageId());

        for (MessengerListener listener : listeners) {
            listener.onMessageRead(message);
        }
    }


    @Override
    public void sendMessage(PMessage message) {
        message.setStatus(PMessageAbs.PMESSAGE_STATUS.STATUS_SENT);
        helper.insert(message.receiverId(), pMessageMapper.transform(message));
        for (MessengerListener listener : listeners) {
            listener.onMessageSent(message);
        }
        if (isAvailable()) {
            deliverMessage(message);
        }
    }

    private void deliverMessage(PMessage message) {
        notifier.sendMessage(MessageUtils.convert(message));
        int statusDelivered = PMessageAbs.PMESSAGE_STATUS.STATUS_DELIVERED;
        message.setStatus(statusDelivered);
        helper.updateStatusByPackageId(message.receiverId(), statusDelivered, message.packageId());
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
        helper.getUndeliveredMessages()
                .subscribe(pMessageAbses -> {
                    for (PMessage message : pMessageAbses) {
                        if (isAvailable()) {
                            deliverMessage(message);
                        }
                    }
                });

        serviceMessagesHelper.getUnreadMessages()
                .subscribe(messages -> {
                    for (ServiceMessageAbs message : messages) {
                        if (isAvailable()) {
                            deliverReadServiceMessage(message.messageId(), message.senderId());
                            serviceMessagesHelper.deleteById(message.id());
                        }
                    }
                });
    }

    private void deliverReadServiceMessage(String messageId, String senderId) {
        Message message = MessageUtils.createReadMessage(senderId, user.getId());
        notifier.sendMessage(message);
    }

    @Override
    public void onDisconnected() {

    }

}
