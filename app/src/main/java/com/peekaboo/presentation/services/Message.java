package com.peekaboo.presentation.services;

/**
 * Created by sebastian on 12.07.16.
 */
public class Message {
    private String sender;
    private String payload;

    public Message(String payload, String sender) {
        this.payload = payload;
        this.sender = sender;
    }

    public String getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "Message{" +
                "sender='" + sender + '\'' +
                ", payload='" + payload + '\'' +
                '}';
    }
}
