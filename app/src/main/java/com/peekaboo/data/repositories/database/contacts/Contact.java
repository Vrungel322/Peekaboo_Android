package com.peekaboo.data.repositories.database.contacts;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nikita on 10.08.2016.
 */
public class Contact extends ContactAbs implements Parcelable {

    private long id;
    private String contactName;
    private String contactSurname;
    private String contactNickname;
    private boolean isOnline;
    private String contactImgUri;
    private String contactId;
    private int unreadMessagesCount = 0;

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

    public void setUnreadMessagesCount(int unreadMessagesCount) {
        this.unreadMessagesCount = unreadMessagesCount;
    }

    public int getUnreadMessagesCount() {
        return unreadMessagesCount;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(contactName);
        dest.writeString(contactSurname);
        dest.writeString(contactNickname);
        dest.writeBooleanArray(new boolean[]{isOnline});
        dest.writeString(contactImgUri);
        dest.writeString(contactId);
        dest.writeInt(unreadMessagesCount);
    }

    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {

        @Override
        public Contact createFromParcel(Parcel source) {
            return new Contact(source);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    private Contact(Parcel in){
        id = in.readLong();
        contactName = in.readString();
        contactSurname = in.readString();
        contactNickname = in.readString();
        isOnline = in.createBooleanArray()[0];
        contactImgUri = in.readString();
        contactId = in.readString();
        unreadMessagesCount = in.readInt();
    }
}
