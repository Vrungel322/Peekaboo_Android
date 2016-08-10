package com.peekaboo.data.repositories.database.contacts;

/**
 * Created by Nikita on 10.08.2016.
 */
public class PContact extends PContactAbs {

    private long id;
    private String contactName;
    private String contactSurname;
    private String contactNickname;
    private boolean isOnline;
    private String contactImgUri;

    public PContact(long id, String contactName, String contactSurname, String contactNickname,
                    boolean isOnline, String contactImgUri) {
        this.id = id;
        this.contactName = contactName;
        this.contactSurname = contactSurname;
        this.contactNickname = contactNickname;
        this.isOnline = isOnline;
        this.contactImgUri = contactImgUri;
    }

    @Override
    public long contactId() {
        return id;
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
