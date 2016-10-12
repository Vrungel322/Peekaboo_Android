package com.peekaboo.domain;

/**
 * Created by st1ch on 12.10.2016.
 */

public class Sms {

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_BODY = "body";

    private long id;
    private String address;
    private long date;
    private String body;

    public Sms(long id, String address, long date, String body) {
        this.id = id;
        this.address = address;
        this.date = date;
        this.body = body;
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
}
