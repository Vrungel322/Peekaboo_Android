package com.peekaboo.domain.usecase;

import android.content.ContentValues;
import android.util.Log;

import com.peekaboo.data.mappers.AbstractMapperFactory;
import com.peekaboo.data.mappers.Mapper;
import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.data.repositories.database.messages.PMessageAbs;
import com.peekaboo.data.repositories.database.messages.PMessageHelper;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.domain.MessageUtils;
import com.peekaboo.presentation.services.INotifier;
import com.peekaboo.presentation.services.Message;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by sebastian on 22.08.16.
 */
@Singleton
public class Messanger implements INotifier<PMessage>,
        INotifier.NotificationListener<Message> {
    private final INotifier<Message> notifier;
    private final PMessageHelper helper;
    private final Mapper<PMessageAbs, ContentValues> pMessageMapper;
    private AccountUser user;

    private Set<NotificationListener<PMessage>> listeners = new HashSet<>();

    @Inject
    public Messanger(INotifier<Message> notifier, PMessageHelper helper, AccountUser user,
                     AbstractMapperFactory abstractMapperFactory) {
        this.notifier = notifier;
        this.helper = helper;
        this.user = user;
        pMessageMapper = abstractMapperFactory.getPMessageMapper();
        notifier.addListener(this);
    }

    public void readMessage(PMessage message) {
        Log.e("messanger", "read " + message);
        int statusRead = PMessageAbs.PMESSAGE_STATUS.STATUS_READ;
        helper.updateStatusByPackageId(message.senderId(), statusRead,
                message.packageId());
        message.setStatus(statusRead);

        for (NotificationListener<PMessage> listener : listeners) {
            listener.onMessageRead(message);
        }
    }

    public Observable<List<PMessageAbs>> getAllMessages(String tableName){
        return helper.getAllMessages(tableName);
    }

    public Observable<List<PMessageAbs>> getUnreadMessages(String tableName){
        return helper.getUnreadMessages(tableName);
    }

    @Override
    public boolean onMessageObtained(Message message) {
        Log.e("messanger", "obtained");
        PMessage pMessage = MessageUtils.convert(user.getId(), message);
        pMessage.setStatus(PMessage.PMESSAGE_STATUS.STATUS_DELIVERED);
        String tableName = message.getParams().get(Message.Params.FROM);
        helper.insert(tableName, pMessageMapper.transform(pMessage));
        boolean isRead = false;
        for (NotificationListener<PMessage> listener : listeners) {
            boolean isMessageSeen = listener.onMessageObtained(pMessage);
            if (isMessageSeen) {
                isRead = true;
            }
        }
        if (isRead) {
            int statusRead = PMessageAbs.PMESSAGE_STATUS.STATUS_READ;
            helper.updateStatusByPackageId(tableName, statusRead,
                    pMessage.packageId());
            pMessage.setStatus(statusRead);

            for (NotificationListener<PMessage> listener : listeners) {
                listener.onMessageRead(pMessage);
            }
        }
        return true;
    }

    @Override
    public void onMessageSent(Message message) {
        Log.e("messanger", "sent " + message);
        String tableName = message.getParams().get(Message.Params.DESTINATION);
        String packageId = message.getParams().get(Message.Params.ID);
        helper.updateStatusByPackageId(tableName, PMessageAbs.PMESSAGE_STATUS.STATUS_DELIVERED,
                packageId);
        PMessage convert = MessageUtils.convert(tableName, message);
        convert.setMine(true);
        for (NotificationListener<PMessage> listener : listeners) {
            listener.onMessageSent(convert);
        }
    }

    /**
     * this method is useless and never called. leave it empty
     *
     * @param message
     */
    @Override
    public void onMessageRead(Message message) {

    }

    @Override
    public void sendMessage(PMessage message) {
        String tableName = message.receiverId();
        message.setStatus(PMessageAbs.PMESSAGE_STATUS.STATUS_SENT);
        Log.e("messanger", "insert");
        helper.insert(tableName, pMessageMapper.transform(message));
        if (isAvailable()) {
            Log.e("message", "send");
            notifier.sendMessage(MessageUtils.convert(message));

        }
        //TODO update view
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
        listeners.add(listener);
    }

    @Override
    public void removeListener(NotificationListener<PMessage> listener) {
        listeners.remove(listener);
    }

    @Override
    public void disconnect() {
        notifier.disconnect();
    }
}
