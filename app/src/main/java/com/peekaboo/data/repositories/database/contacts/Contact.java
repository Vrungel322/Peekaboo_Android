package com.peekaboo.data.repositories.database.contacts;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.peekaboo.data.repositories.database.utils_db.Db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private List<String> chatContacts;

    public Contact(long id, String contactName, String contactSurname, String contactNickname,
                   boolean isOnline, String contactImgUri, String contactId, List<String> chatContacts) {
        this.id = id;
        this.contactName = contactName;
        this.contactSurname = contactSurname;
        this.contactNickname = contactNickname;
        this.isOnline = isOnline;
        this.contactImgUri = contactImgUri;
        this.contactId = contactId;
        this.chatContacts = chatContacts;
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
    public List<String> chatContacts() {
        return chatContacts;
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
        dest.writeStringList(chatContacts);
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

    private Contact(Parcel in) {
        id = in.readLong();
        contactName = in.readString();
        contactSurname = in.readString();
        contactNickname = in.readString();
        isOnline = in.createBooleanArray()[0];
        contactImgUri = in.readString();
        contactId = in.readString();
        ArrayList<String> strings = new ArrayList<>();
        in.readStringList(strings);
        chatContacts = (!strings.isEmpty()) ? strings : null;
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



    public static Contact fetchPContact(Cursor cursor) {
        long id = Db.getLong(cursor, ContactAbs.CONTACT_ID);
        String contactId = Db.getString(cursor, ContactAbs.CONTACT_ID);
        String contactName = Db.getString(cursor, ContactAbs.CONTACT_NAME);
        String contactSurname = Db.getString(cursor, ContactAbs.CONTACT_SURNAME);
        String contactNickname = Db.getString(cursor, ContactAbs.CONTACT_NICKNAME);
        boolean isOnline = Db.getBoolean(cursor, ContactAbs.CONTACT_IS_ONLINE);
        String contactImgUri = Db.getString(cursor, ContactAbs.CONTACT_IMG_URI);
        List<String> chatContacts;
        String contactsString = Db.getString(cursor, ContactAbs.CHAT_CONTACTS);
        if (contactsString == null) {
            chatContacts = null;
        } else {
            contactsString = contactsString.substring(1, contactsString.length() - 1);
            chatContacts = Arrays.asList(contactsString.split(","));
        }

        return new Contact(id, contactName, contactSurname, contactNickname, isOnline, contactImgUri, contactId, chatContacts);
    }

    public static final class Builder{
        private final ContentValues cv = new ContentValues();

        public Builder id(long id){
            cv.put(ID, id);
            return this;
        }

        public Builder contactId(String contactId){
            cv.put(CONTACT_ID, contactId);
            return this;
        }

        public Builder contactName(String contactName){
            cv.put(CONTACT_NAME, contactName);
            return this;
        }

        public Builder contactSurname(String contactSurname){
            cv.put(CONTACT_SURNAME, contactSurname);
            return this;
        }

        public Builder contactNickname(String contactNickname){
            cv.put(CONTACT_NICKNAME, contactNickname);
            return this;
        }

        public Builder isOnline(boolean isOnline){
            cv.put(CONTACT_IS_ONLINE, isOnline);
            return this;
        }

        public Builder contactImgUri(String contactImgUri){
            cv.put(CONTACT_IMG_URI, contactImgUri);
            return this;
        }

        public Builder chatContacts(List<String> chatContacts){
            if (chatContacts != null) {
                cv.put(CHAT_CONTACTS, chatContacts.toString().replace(" ", ""));
            } else {
                cv.putNull(CHAT_CONTACTS);
            }
            return this;
        }

        public ContentValues build(){
            return cv;
        }
    }

}
