package com.peekaboo.data.repositories.database.contacts;

/**
 * Created by Nikita on 10.08.2016.
 */
public class Contact extends ContactAbs {

    private long id;
    private String contactName;
    private String contactSurname;
    private String contactNickname;
    private boolean isOnline;
    private String contactImgUri;
    private String contactId;

    public Contact(long id, String contactName, String contactSurname, String contactNickname,
                   boolean isOnline, String contactImgUri, String contactId) {
        this.id = id;
        this.contactName = contactName;
        this.contactSurname = contactSurname;
        this.contactNickname = contactNickname;
        this.isOnline = isOnline;
        this.contactImgUri = contactImgUri;
        this.contactId = contactId;
    }

    @Override
    public long id() {
        return id;
    }

    @Override
    public String contactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    @Override
    public String contactName() {
        return contactName;
    }

    @Override
    public String contactSurname() {
        return contactSurname;
    }

    @Override
    public String contactNickname() {
        return contactNickname;
    }

    @Override
    public boolean isOnline() {
        return isOnline;
    }

    @Override
    public String contactImgUri() {
        return contactImgUri;
    }
}
