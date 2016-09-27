package com.peekaboo.data.repositories.database.messages;

/**
 * Created by st1ch on 23.07.2016.
 */
public class PMessage extends PMessageAbs {

    public static final String DIVIDER = "  ";
    public static final String ERROR = "<@error@>";
    private long id;
    private boolean isMine;
    private String messageBody;
    private int mediaType;
    private long timestamp;
    private int status;
    private String receiverId;
    private String senderId;

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
    public String toString() {
        return "{ id=" + id + ", body=" + messageBody + ", isMine=" + isMine + ", status=" + status() + " }";
    }

    public boolean isDownloaded() {
        return messageBody.contains(DIVIDER);
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    @Override
    public long id() {
        return id;
    }

    @Override
    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    @Override
    public int mediaType() {
        return mediaType;
    }

    @Override
    public String messageBody() {
        return messageBody.replace(ERROR, "");
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

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PMessage) {
            PMessage message = (PMessage) o;
            return message.id() == id() &&
                    message.senderId().equals(senderId) &&
                    message.receiverId().equals(receiverId());
        }
        return false;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
