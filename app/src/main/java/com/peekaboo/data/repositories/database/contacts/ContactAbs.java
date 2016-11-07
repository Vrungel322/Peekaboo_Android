package com.peekaboo.data.repositories.database.contacts;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.auto.value.AutoValue;
import com.peekaboo.data.repositories.database.utils_db.Db;

import java.util.List;

/**
 * Created by Nikita on 10.08.2016.
 */
public abstract class ContactAbs {

    public static final String ID = "_id";
    public static final String CONTACT_NAME = "CONTACT_NAME";
    public static final String CONTACT_SURNAME = "CONTACT_SURNAME";
    public static final String CONTACT_NICKNAME = "CONTACT_NICKNAME";
    public static final String CONTACT_IS_ONLINE = "CONTACT_IS_ONLINE";
    public static final String CONTACT_IMG_URI = "CONTACT_IMG_URI";
    public static final String CONTACT_ID = "CONTACT_ID";
    public static final String CHAT_CONTACTS = "CHAT_CONTACTS";


    public abstract long id();
    public abstract String contactId();
    public abstract String contactName();
    public abstract String contactSurname();
    public abstract String contactNickname();
    public abstract boolean isOnline();
    public abstract String contactImgUri();
    public abstract List<String> chatContacts();

}
