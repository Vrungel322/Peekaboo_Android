package com.peekaboo.presentation.presenters;

import android.util.Log;

import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.data.repositories.database.contacts.ContactAbs;
import com.peekaboo.data.repositories.database.contacts.PContactHelper;
import com.peekaboo.domain.UserMessageMapper;
import com.peekaboo.domain.subscribers.BaseProgressSubscriber;
import com.peekaboo.domain.usecase.GetContactFromDbUseCase;
import com.peekaboo.presentation.views.IContactsView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Nikita on 11.08.2016.
 */
public class ContactPresenter extends ProgressPresenter<IContactsView> implements IContactPresenter {

    private PContactHelper contactHelper;
    private GetContactFromDbUseCase getContactFromDbUseCase;

    @Inject
    public ContactPresenter(PContactHelper contactHelper,
                            UserMessageMapper errorHandler,
                            GetContactFromDbUseCase getContactFromDbUseCase) {
        super(errorHandler);
        this.contactHelper = contactHelper;
        this.getContactFromDbUseCase = getContactFromDbUseCase;
    }

    @Override
    public void onCreate() {
        loadContactsList();
    }

    @Override
    public void onDestroy() {
        getContactFromDbUseCase.unsubscribe();
        unbind();
    }

    @Override
    public void loadContactsList() {
        if (getContactFromDbUseCase.isWorking()) {
            getContactFromDbUseCase.execute(getContactsFromDbSubscriber());
        }
    }

    private BaseProgressSubscriber<List<Contact>> getContactsFromDbSubscriber() {
        return new BaseProgressSubscriber<List<Contact>>(this) {
            @Override
            public void onNext(List<Contact> contacts) {
                super.onNext(contacts);
                IContactsView view = getView();
                if (view != null) {
                    view.showContactsList(contacts);
                    // for testing
                    getAllTableAsString();
                }
            }
        };
    }

    private void getAllTableAsString() {
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
