package com.peekaboo.data.mappers;

import android.content.ContentValues;

import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.data.repositories.database.contacts.ContactAbs;

/**
 * Created by Nikita on 11.08.2016.
 */
public class PContactMapper implements Mapper<Contact, ContentValues> {
    @Override
    public ContentValues transform(Contact obj) throws RuntimeException {
        return new ContactAbs.Builder()
                .contactName(obj.contactName())
                .contactSurname(obj.contactSurname())
                .contactNickname(obj.contactNickname())
                .isOnline(obj.isOnline())
                .contactImgUri(obj.contactImgUri())
                .build();
    }
}
