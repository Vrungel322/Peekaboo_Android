package com.peekaboo.data.mappers;

import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.data.rest.entity.ContactEntity;

/**
 * Created by Nikita on 20.09.2016.
 */
public class ContactEntityToContactMapper implements Mapper<ContactEntity, Contact> {
    @Override
    public Contact transform(ContactEntity obj) throws RuntimeException {
        return new Contact(0, obj.getName(), "", obj.getUsername(), false, "", String.valueOf(obj.getId()));
    }
}
