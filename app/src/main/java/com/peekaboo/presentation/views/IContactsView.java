package com.peekaboo.presentation.views;

import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.presentation.pojo.PhoneContactPOJO;

import java.util.List;

/**
 * Created by Nikita on 13.09.2016.
 */
public interface IContactsView extends IProgressView {
    void showContactsList(List<Contact> listContact);
    void showPhoneContactList(List<PhoneContactPOJO> response);
}
