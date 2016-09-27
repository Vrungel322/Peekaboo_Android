package com.peekaboo.data.mappers;

import android.content.ContentValues;

import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.data.repositories.database.messages.PMessageAbs;
import com.peekaboo.data.rest.entity.ContactEntity;
import com.peekaboo.domain.User;
import com.peekaboo.data.rest.entity.UserEntity;
import com.peekaboo.presentation.services.Message;

/**
 * Created by sebastian on 28.06.16.
 */
public class MapperFactory implements AbstractMapperFactory {
    private String url;

    public MapperFactory(String url) {
        this.url = url;
    }

    @Override
    public Mapper<UserEntity, User> getUserMapper() {
        return new UserMapper();
    }

    @Override
    public Mapper<byte[], Message> getByteToMessageMapper() {
        return new ByteArrayToMessageMapper();
    }

    @Override
    public Mapper<Message, byte[]> getMessageToByteMapper() {
        return new MessageToByteArrayMapper();
    }

    public Mapper<PMessageAbs, ContentValues> getPMessageMapper() {
        return new PMessageMapper();
    }

//    public Mapper<Contact, ContentValues> getPContactMapper() {
//        return new PContactMapper();
//    }

    @Override
    public Mapper<ContactEntity, Contact> getContactEntityMapper() {
        return new ContactEntityToContactMapper(url);
    }

    @Override
    public Mapper<Contact, ContentValues> getContactToContentValueMapper() {
        return new ContactToContentValueMapper();
    }

}
