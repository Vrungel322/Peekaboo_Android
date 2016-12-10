package com.peekaboo.data.repositories.database.contacts;

import android.content.ContentValues;

import com.google.auto.value.AutoValue;
import com.peekaboo.data.GroupChatCreationEntity;
import com.peekaboo.domain.GroupChat;

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
    public static final String GROUP_CHAT_MEMBERS = "GROUP_CHAT_MEMBERS";

    public abstract long id();
    public abstract String contactId();
    public abstract String contactName();
    public abstract String contactSurname();
    public abstract String contactNickname();
    public abstract boolean isOnline();
    public abstract String contactImgUri();
    public abstract GroupChat groupChat();
    public abstract boolean isGroupChat();
}
