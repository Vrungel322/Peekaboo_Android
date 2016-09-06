package com.peekaboo.presentation.services;

import com.peekaboo.data.repositories.database.messages.PMessage;

import java.util.List;

import rx.Observable;

/**
 * Created by sebastian on 29.08.16.
 */
public interface IMessenger extends INotifier<PMessage> {

    void readMessage(PMessage message);

    Observable<List<PMessage>> getAllMessages(String tableName);

    Observable<List<PMessage>> getUnreadMessages(String tableName);

    void addMessageListener(MessengerListener messengerListener);

    void removeMessageListener(MessengerListener messengerListener);

    interface MessengerListener {
        void onMessageUpdated(PMessage message);

        int willChangeStatus(PMessage message);

    }

}
