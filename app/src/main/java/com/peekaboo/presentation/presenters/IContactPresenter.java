package com.peekaboo.presentation.presenters;

import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.presentation.views.IContactsView;

import rx.functions.Action1;

/**
 * Created by Nikita on 11.08.2016.
 */
public interface IContactPresenter extends IPresenter<IContactsView> {

//    void createTable(String tableName);
    void onButtonAddContactClick(Contact contact);
    void onAllContactsLoading(Action1 adapter);

}
