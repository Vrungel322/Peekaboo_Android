package com.peekaboo.data.mappers;

import android.content.ContentValues;

import com.peekaboo.data.repositories.database.contacts.PContact;
import com.peekaboo.data.repositories.database.contacts.PContactAbs;

/**
 * Created by Nikita on 11.08.2016.
 */
public class PContactMapper implements Mapper<PContact, ContentValues> {
    @Override
    public ContentValues transform(PContact obj) throws RuntimeException {
        return new PContactAbs.Builder()
                .contactName(obj.contactName())
                .contactSurname(obj.contactSurname())
                .contactNickname(obj.contactNickname())
                .isOnline(obj.isOnline())
                .contactImgUri(obj.contactImgUri())
                .contactId(obj.contactId())
                .build();
    }
}
