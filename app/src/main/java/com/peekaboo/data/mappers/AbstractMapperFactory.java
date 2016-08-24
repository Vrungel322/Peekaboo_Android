package com.peekaboo.data.mappers;


import android.content.ContentValues;

import com.peekaboo.data.repositories.database.contacts.PContact;
import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.data.repositories.database.messages.PMessageAbs;
import com.peekaboo.data.rest.entity.UserEntity;
import com.peekaboo.domain.AccountUser;
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

    Mapper<PContact, ContentValues> getPContactMapper();
}
