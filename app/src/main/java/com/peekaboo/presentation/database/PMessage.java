package com.peekaboo.presentation.database;

/**
 * Created by Nikita on 18.07.2016.
 */
public class PMessage {
    private String packageId;
    private boolean isMine;
    private String messageBody;
    private Long timestamp;
    private boolean isSend;
    private boolean isDelivered;
    private boolean isRead;

    public PMessage(String packageId, boolean isMine, String messageBody,
                    Long timestamp, boolean isSend, boolean isDelivered, boolean isRead) {
        this.packageId = packageId;
        this.isMine = isMine;
        this.messageBody = messageBody;
        this.timestamp = timestamp;
        this.isSend = isSend;
        this.isDelivered = isDelivered;
        this.isRead = isRead;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    public boolean isDelivered() {
        return isDelivered;
    }

    public void setDelivered(boolean delivered) {
        isDelivered = delivered;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
