package com.peekaboo.domain;

/**
 * Created by st1ch on 12.10.2016.
 */

public class Sms {

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_BODY = "body";
    public static final String COLUMN_TYPE = "type";

    private long id;
    private String address;
    private long date;
    private String body;
    private int type;
//    Explaining of type:
//    MESSAGE_TYPE_ALL    = 0;
//    MESSAGE_TYPE_INBOX  = 1; input
//    MESSAGE_TYPE_SENT   = 2; output = isMine
//    MESSAGE_TYPE_DRAFT  = 3;
//    MESSAGE_TYPE_OUTBOX = 4;
//    MESSAGE_TYPE_FAILED = 5; // for failed outgoing messages
//    MESSAGE_TYPE_QUEUED = 6; // for messages to send later

    public Sms(long id, String address, long date, String body, int type) {
        this.id = id;
        this.address = address;
        this.date = date;
        this.body = body;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
