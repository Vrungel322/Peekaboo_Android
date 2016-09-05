package com.peekaboo.data.repositories.database.messages;

import android.support.annotation.Nullable;

/**
 * Created by st1ch on 23.07.2016.
 */
public class PMessage extends PMessageAbs {

    private long id;
    private boolean isMine;
    private String messageBody;
    private int mediaType;
    private long timestamp;
    private int status;
    private String receiverId;
    private String senderId;

    @Override
    public String toString() {
        return "{ id=" + id + ", body=" + messageBody + ", isMine=" + isMine + " }";
    }

    public PMessage(boolean isMine, int mediaType, String messageBody, long timestamp,
                    int status, String receiverId, String senderId) {
        this.isMine = isMine;
        this.mediaType = mediaType;
        this.messageBody = messageBody;
        this.timestamp = timestamp;
        this.status = status;
        this.receiverId = receiverId;
        this.senderId = senderId;
    }

    @Override
    public long id() {
        return id;
    }

    @Override
    public boolean isMine() {
        return isMine;
    }

    @Override
    public int mediaType() {
        return mediaType;
    }

    @Override
    public String messageBody() {
        return messageBody;
    }

    @Override
    public long timestamp() {
        return timestamp;
    }

    @Override
    public int status() {
        return status;
    }

    @Override
    public String receiverId() {
        return receiverId;
    }

    @Override
    public String senderId() {
        return senderId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
