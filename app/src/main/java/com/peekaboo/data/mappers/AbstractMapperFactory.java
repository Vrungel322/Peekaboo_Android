package com.peekaboo.data.mappers;


import android.content.ContentValues;

import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.domain.User;
import com.peekaboo.presentation.services.Message;

/**
 * Created by sebastian on 10.06.16.
 */
public interface AbstractMapperFactory {
    Mapper<String, User> getUserMapper();

    Mapper<Message, String> getMessageToStringMapper();

    Mapper<String, Message> getStringToMessageMapper();

    Mapper<byte[], Message> getByteToMessageMapper();

    Mapper<Message, byte[]> getMessageToByteMapper();

    Mapper<PMessage, ContentValues> getPMessageMapper();
}
