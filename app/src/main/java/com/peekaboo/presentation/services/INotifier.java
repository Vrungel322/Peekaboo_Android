package com.peekaboo.presentation.services;

/**
 * Created by sebastian on 12.07.16.
 */
public interface INotifier<T> {

    void sendMessage(T message);

    boolean isAvailable();

    void tryConnect(String authorization);

    void addListener(NotificationListener<T> listener);

    void removeListener(NotificationListener<T> listener);

    void disconnect();

    interface NotificationListener<T> {

        void onMessageObtained(T message);

        void onConnected();

        void onDisconnected();
    }
}
