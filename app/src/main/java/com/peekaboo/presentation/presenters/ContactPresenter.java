package com.peekaboo.presentation.presenters;

import android.content.Context;
import android.util.Log;

import com.peekaboo.data.mappers.AbstractMapperFactory;
import com.peekaboo.data.repositories.database.contacts.PContact;
import com.peekaboo.data.repositories.database.contacts.PContactAbs;
import com.peekaboo.data.repositories.database.contacts.PContactHelper;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by Nikita on 11.08.2016.
 */
public class ContactPresenter implements IContactPresenter {

    private Context context;
    private AbstractMapperFactory mapperFactory;
    private PContactHelper pContactHelper;

    @Inject
    public ContactPresenter(Context context, AbstractMapperFactory mapperFactory, PContactHelper pContactHelper) {
        this.context = context;
        this.mapperFactory = mapperFactory;
        this.pContactHelper = pContactHelper;
    }

    @Override
    public void createTable(String tableName) {
//        pContactHelper.createTable(tableName);
    }

    @Override
    public void insertContactToTable(String tableName, PContact contact) {
        pContactHelper.insert(mapperFactory.getPContactMapper().transform(contact));
    }

    @Override
    public void getAllContacts(String tableName, Action1 adapter) {
        //TODO: get all contacts
    }

    @Override
    public void dropTableAndCreate(String tableName) {
        pContactHelper.dropTableAndCreate();
    }

    @Override
    public void getAllTableAsString(String tableName) {
        pContactHelper.getAllContacts()
                .subscribe(pContactAbses -> {
                    for (PContactAbs pContact : pContactAbses){
                        Log.wtf("DB_LOG", "ID: " + pContact.contactId()
                                + "; CONTACT_NAME: " + pContact.contactName()
                                + "; CONTACT_SURNAME: " + pContact.contactSurname()
                                + "; CONTACT_NICKNAME: " + pContact.contactNickname()
                                + "; CONTACT_IS_ONLINE: " + pContact.isOnline()
                                + "; CONTACT_IMG_URI: " + pContact.contactImgUri());
                    }
                });
    }
}
