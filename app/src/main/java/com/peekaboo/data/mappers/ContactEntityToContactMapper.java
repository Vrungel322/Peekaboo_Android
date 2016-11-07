package com.peekaboo.data.mappers;

import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.data.rest.entity.ContactEntity;
import com.peekaboo.utils.Utility;

/**
 * Created by Nikita on 20.09.2016.
 */
public class ContactEntityToContactMapper implements Mapper<ContactEntity, Contact> {
    String url;

    public ContactEntityToContactMapper(String url) {
        this.url = url;
    }

    @Override
    public Contact transform(ContactEntity obj) throws RuntimeException {
        return new Contact(0, obj.getName(), obj.getSurname(), obj.getNickname(), Utility.convertIntToBoolean(obj.getState()),
                url + obj.getId(), obj.getId(), null);
    }
}
