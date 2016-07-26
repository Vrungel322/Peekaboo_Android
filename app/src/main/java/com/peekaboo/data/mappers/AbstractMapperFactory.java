package com.peekaboo.data.mappers;


import com.peekaboo.data.rest.entity.UserEntity;
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
}
