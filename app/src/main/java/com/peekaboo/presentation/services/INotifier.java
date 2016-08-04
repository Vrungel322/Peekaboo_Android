package com.peekaboo.presentation.services;

/**
 * Created by sebastian on 12.07.16.
 */
public interface INotifier {
    void sendMessage(Message message);
    boolean isAvailable();
    void tryConnect();

    void sendFile(Message message, String file);
    void addListener(NotificationListener listener);

    void removeListener(NotificationListener listener);

    void disconnect();

    interface NotificationListener {
        void onMessageObtained(Message message);
    }
}
