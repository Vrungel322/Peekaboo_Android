package com.peekaboo.data.repositories.database;

/**
 * Created by st1ch on 23.07.2016.
 */
public class PMessage extends PMessageAbs {

    private long id;
    private String packageId;
    private boolean isMine;
    private String messageBody;
    private long timestamp;
    private boolean isSent;
    private boolean isDelivered;
    private boolean isRead;

    public PMessage(String packageId, boolean isMine, String messageBody, long timestamp,
                    boolean isSent, boolean isDelivered, boolean isRead) {
        this.packageId = packageId;
        this.isMine = isMine;
        this.messageBody = messageBody;
        this.timestamp = timestamp;
        this.isSent = isSent;
        this.isDelivered = isDelivered;
        this.isRead = isRead;
    }

    @Override
    public long id() {
        return id;
    }

    @Override
    public String packageId() {
        return packageId;
    }

    @Override
    public boolean isMine() {
        return isMine;
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
    public boolean isSent() {
        return isSent;
    }

    @Override
    public boolean isDelivered() {
        return isDelivered;
    }

    @Override
    public boolean isRead() {
        return isRead;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public void setDelivered(boolean delivered) {
        isDelivered = delivered;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
