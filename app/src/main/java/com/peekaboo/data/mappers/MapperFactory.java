package com.peekaboo.data.mappers;

import android.content.ContentValues;

import com.peekaboo.domain.User;
import com.peekaboo.data.repositories.database.PMessage;
import com.peekaboo.presentation.services.Message;

/**
 * Created by sebastian on 28.06.16.
 */
public class MapperFactory implements AbstractMapperFactory {
    @Override
    public Mapper<String, User> getUserMapper() {
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

}
