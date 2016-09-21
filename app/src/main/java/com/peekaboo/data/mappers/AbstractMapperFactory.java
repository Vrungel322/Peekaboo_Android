package com.peekaboo.data.mappers;


import android.content.ContentValues;

import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.data.repositories.database.messages.PMessageAbs;
import com.peekaboo.data.rest.entity.ContactEntity;
import com.peekaboo.data.rest.entity.UserEntity;
import com.peekaboo.domain.User;
import com.peekaboo.presentation.services.Message;

/**
 * Created by sebastian on 10.06.16.
 */
public interface AbstractMapperFactory {
    Mapper<UserEntity, User> getUserMapper();

    Mapper<byte[], Message> getByteToMessageMapper();

    Mapper<Message, byte[]> getMessageToByteMapper();

    Mapper<PMessageAbs, ContentValues> getPMessageMapper();

    Mapper<Contact, ContentValues> getPContactMapper();

    Mapper<ContactEntity, Contact> getContactEntityMapper();
}
