package com.peekaboo.data.mappers;

import android.content.ContentValues;

import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.data.repositories.database.contacts.ContactAbs;

/**
 * Created by Nikita on 21.09.2016.
 */
public class ContactToContentValueMapper implements Mapper<Contact, ContentValues> {
    @Override
    public ContentValues transform(Contact obj) throws RuntimeException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactAbs.CONTACT_ID, obj.contactId());
        contentValues.put(ContactAbs.CONTACT_NAME , obj.contactName());
        contentValues.put(ContactAbs.CONTACT_SURNAME , obj.contactSurname());
        contentValues.put(ContactAbs.CONTACT_NICKNAME , obj.contactNickname());
        contentValues.put(ContactAbs.CONTACT_IS_ONLINE , obj.isOnline());
        contentValues.put(ContactAbs.CONTACT_IMG_URI , obj.contactImgUri());
        return contentValues;
    }
}
