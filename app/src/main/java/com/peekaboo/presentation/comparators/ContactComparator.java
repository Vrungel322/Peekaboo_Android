package com.peekaboo.presentation.comparators;

import com.peekaboo.data.repositories.database.contacts.Contact;

import java.util.Comparator;

/**
 * Created by st1ch on 27.09.2016.
 */

public class ContactComparator implements Comparator<Contact> {
    @Override
    public int compare(Contact lhs, Contact rhs) {
        return lhs.contactName().toLowerCase().compareTo(rhs.contactName().toLowerCase());
    }
}
