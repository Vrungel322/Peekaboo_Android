package com.peekaboo.data.mappers;


import android.content.ContentValues;

import com.peekaboo.domain.User;
import com.peekaboo.data.repositories.database.PMessage;
import com.peekaboo.presentation.services.Message;

/**
 * Created by sebastian on 10.06.16.
 */
public interface AbstractMapperFactory {
    Mapper<String, User> getUserMapper();

    Mapper<Message, String> getMessageToStringMapper();
    Mapper<String, Message> getStringToMessageMapper();
    Mapper<PMessage, ContentValues> getPMessageMapper();
}
