package com.peekaboo.presentation.services;

/**
 * Created by sebastian on 12.07.16.
 */
public interface INotifier {
    void sendMessage(Message message);
//    void sendBinaryMessage(byte[] s, String receiver);
    boolean isAvailable();
    void tryConnect();

    void addListener(NotificationListener listener);

    void removeListener(NotificationListener listener);

    interface NotificationListener {
        void onMessageObtained(Message message);
    }
}
