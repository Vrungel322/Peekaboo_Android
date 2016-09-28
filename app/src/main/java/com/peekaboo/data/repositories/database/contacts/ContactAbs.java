package com.peekaboo.data.repositories.database.contacts;

import android.content.ContentValues;

import com.google.auto.value.AutoValue;

/**
 * Created by Nikita on 10.08.2016.
 */
@AutoValue
public abstract class ContactAbs {

    public static final String ID = "_id";
    public static final String CONTACT_NAME = "CONTACT_NAME";
    public static final String CONTACT_SURNAME = "CONTACT_SURNAME";
    public static final String CONTACT_NICKNAME = "CONTACT_NICKNAME";
    public static final String CONTACT_IS_ONLINE = "CONTACT_IS_ONLINE";
    public static final String CONTACT_IMG_URI = "CONTACT_IMG_URI";
    public static final String CONTACT_ID = "CONTACT_ID";

    public abstract long id();
    public abstract String contactId();
    public abstract String contactName();
    public abstract String contactSurname();
    public abstract String contactNickname();
    public abstract boolean isOnline();
    public abstract String contactImgUri();

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

        public ContentValues build(){
            return cv;
        }
    }
}
