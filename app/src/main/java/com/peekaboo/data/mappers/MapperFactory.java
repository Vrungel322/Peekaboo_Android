package com.peekaboo.data.mappers;

import android.content.ContentValues;

import com.peekaboo.data.repositories.database.contacts.PContact;
import com.peekaboo.domain.User;
import com.peekaboo.data.rest.entity.UserEntity;
import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.presentation.services.Message;

/**
 * Created by sebastian on 28.06.16.
 */
public class MapperFactory implements AbstractMapperFactory {
    @Override
    public Mapper<UserEntity, User> getUserMapper() {
        return new UserMapper();
    }

    public Mapper<Message, String> getMessageToStringMapper() {
        return new MessageJsonMapper();
    }

    public Mapper<String, Message> getStringToMessageMapper() {
        return new JsonMessageMapper();
    }

    @Override
    public Mapper<byte[], Message> getByteToMessageMapper() {
        return new ByteArrayToMessageMapper();
    }

    @Override
    public Mapper<Message, byte[]> getMessageToByteMapper() {
        return new MessageToByteArrayMapper();
    }

    public Mapper<PMessage, ContentValues> getPMessageMapper() {
        return new PMessageMapper();
    }

    public Mapper<PContact, ContentValues> getPContactMapper() {
        return new PContactMapper();
    }

}
