package com.peekaboo.data.mappers;

import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.data.rest.entity.ContactEntity;
import com.peekaboo.utils.Utility;

/**
 * Created by Nikita on 20.09.2016.
 */
public class ContactEntityToContactMapper implements Mapper<ContactEntity, Contact> {
    @Override
    public Contact transform(ContactEntity obj) throws RuntimeException {
        return new Contact(obj.getId(), obj.getName(), obj.getSurname(), obj.getNickname(), Utility.convertIntToBoolean(obj.getState()),
                obj.getImgUri(), String.valueOf(obj.getId()));
    }
}
