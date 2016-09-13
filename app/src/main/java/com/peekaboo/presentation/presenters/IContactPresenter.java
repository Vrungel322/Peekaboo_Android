package com.peekaboo.presentation.presenters;

import com.peekaboo.data.repositories.database.contacts.PContact;

import rx.functions.Action1;

/**
 * Created by Nikita on 11.08.2016.
 */
public interface IContactPresenter {
    void createTable(String tableName);
    void insertContactToTable(String tableName, PContact contact);
    void getAllContacts(String tableName, Action1 adapter);
    void dropTableAndCreate(String tableName);
    void getAllTableAsString(String tableName);
    void makeContactsQuery();
}
