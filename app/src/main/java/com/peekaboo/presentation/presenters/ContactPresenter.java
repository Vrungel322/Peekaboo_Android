package com.peekaboo.presentation.presenters;

import android.util.Log;

import com.peekaboo.data.mappers.AbstractMapperFactory;
import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.data.repositories.database.contacts.ContactAbs;
import com.peekaboo.data.repositories.database.contacts.ContactHelper;
import com.peekaboo.presentation.views.IContactsView;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by Nikita on 11.08.2016.
 */
public class ContactPresenter extends BasePresenter<IContactsView> implements IContactPresenter {

    AbstractMapperFactory mapperFactory;
    private ContactHelper contactHelper;

    @Inject
    public ContactPresenter(AbstractMapperFactory mapperFactory,
                            ContactHelper contactHelper) {
        this.mapperFactory = mapperFactory;
        this.contactHelper = contactHelper;
    }

    @Override
    public void onButtonAddContactClick(Contact contact) {
        contactHelper.insert(mapperFactory.getPContactMapper().transform(contact));
    }

    @Override
    public void onAllContactsLoading(Action1 adapter) {

    }

    public void getAllTableAsString() {
        contactHelper.getAllContacts()
                .subscribe(pContactAbses -> {
                    for (ContactAbs pContact : pContactAbses) {
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
