package com.peekaboo.data.repositories.database.contacts;

import com.peekaboo.domain.GroupChat;

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
    private GroupChat groupChat;

    public Contact(long id, String contactName, String contactSurname, String contactNickname,
                   boolean isOnline, String contactImgUri, String contactId, GroupChat groupChat) {
        this.id = id;
        this.contactName = contactName;
        this.contactSurname = contactSurname;
        this.contactNickname = contactNickname;
        this.isOnline = isOnline;
        this.contactImgUri = contactImgUri;
        this.contactId = contactId;
        this.groupChat = groupChat;
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

    @Override
    public GroupChat groupChat() {
        return groupChat;
    }

    @Override
    public boolean isGroupChat() {
        return groupChat() != null;
    }

    public String contactImgUriSmall() {
        return contactImgUri + "/2";
    }

    public String contactImgUriMiddle() {
        return contactImgUri + "/1";
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id + " " + contactId() + " " + contactNickname();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        return contactId.equals(contact.contactId);

    }

    @Override
    public int hashCode() {
        return contactId.hashCode();
    }

}
