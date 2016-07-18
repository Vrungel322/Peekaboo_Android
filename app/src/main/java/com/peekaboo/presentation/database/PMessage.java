package com.peekaboo.presentation.database;

/**
 * Created by Nikita on 18.07.2016.
 */
public class PMessage {
    private String idPack;
    private String mesBody;
    private Long timestamp;
    private int send;
    private int delivered;
    private int read;

    public PMessage(String mesBody, int delivered, int read, int send, Long timestamp, String idPack) {
        this.delivered = delivered;
        this.idPack = idPack;
        this.mesBody = mesBody;
        this.read = read;
        this.send = send;
        this.timestamp = timestamp;
    }

    public int getDelivered() {
        return delivered;
    }

    public void setDelivered(int delivered) {
        this.delivered = delivered;
    }

    public String getIdPack() {
        return idPack;
    }

    public void setIdPack(String idPack) {
        this.idPack = idPack;
    }

    public String getMesBody() {
        return mesBody;
    }

    public void setMesBody(String mesBody) {
        this.mesBody = mesBody;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public int getSend() {
        return send;
    }

    public void setSend(int send) {
        this.send = send;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
