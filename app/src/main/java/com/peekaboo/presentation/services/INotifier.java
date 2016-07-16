package com.peekaboo.presentation.services;

/**
 * Created by sebastian on 12.07.16.
 */
public interface INotifier {
    void sendMessage(Message message);
    boolean isAvailable();
    void tryConnect();

    interface NotificationListener {
        void onMessageObtained(Message message);
    }
}
