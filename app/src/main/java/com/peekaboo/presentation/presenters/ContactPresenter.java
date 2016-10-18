package com.peekaboo.presentation.presenters;

import android.util.Log;

import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.domain.UserMessageMapper;
import com.peekaboo.domain.subscribers.BaseProgressSubscriber;
import com.peekaboo.domain.usecase.GetContactFromDbUseCase;
import com.peekaboo.domain.usecase.GetPhoneContactListUseCase;
import com.peekaboo.presentation.comparators.ContactComparator;
import com.peekaboo.presentation.pojo.PhoneContactPOJO;
import com.peekaboo.presentation.views.IContactsView;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Nikita on 11.08.2016.
 */
@Singleton
public class ContactPresenter extends ProgressPresenter<IContactsView> implements IContactPresenter {

    private GetContactFromDbUseCase getContactFromDbUseCase;
    private GetPhoneContactListUseCase getPhoneContactListUseCase;

    @Inject
    public ContactPresenter(UserMessageMapper errorHandler,
                            GetContactFromDbUseCase getContactFromDbUseCase,
                            GetPhoneContactListUseCase getPhoneContactListUseCase) {
        super(errorHandler);
        this.getContactFromDbUseCase = getContactFromDbUseCase;
        this.getPhoneContactListUseCase = getPhoneContactListUseCase;
    }

    @Override
    public void onCreate() {
        loadContactsList();
        loadPhoneContactList();
    }

    @Override
    public void onDestroy() {
        getContactFromDbUseCase.unsubscribe();
        getPhoneContactListUseCase.unsubscribe();
    }

    private void loadContactsList() {
        getContactFromDbUseCase.execute(getContactsFromDbSubscriber());
    }

    private void loadPhoneContactList(){
        getPhoneContactListUseCase.execute(getContactsFromContactBook());
    }

    public BaseProgressSubscriber<List<PhoneContactPOJO>> getContactsFromContactBook() {
        return new BaseProgressSubscriber<List<PhoneContactPOJO>>(this){
            @Override
            public void onNext(List<PhoneContactPOJO> response) {
                super.onNext(response);
                for (PhoneContactPOJO p : response) {
                    //TODO : sent response to view
                    Log.wtf("PhoneContactPOJO : ","name : " + p.getName() + " phone : " + p.getPhone());
                }
            }
        };
    }

    private BaseProgressSubscriber<List<Contact>> getContactsFromDbSubscriber() {
        return new BaseProgressSubscriber<List<Contact>>(this) {
            @Override
            public void onNext(List<Contact> contacts) {
                super.onNext(contacts);
                IContactsView view = getView();
                if (view != null) {
                    Collections.sort(contacts, new ContactComparator());
                    view.showContactsList(contacts);
                    // for testing
//                    getAllTableAsString();
                }
            }
        };
    }
//
//    private void getAllTableAsString() {
//        contactHelper.getAllContacts()
//                .subscribe(pContactAbses -> {
//                    for (ContactAbs pContact : pContactAbses) {
//                        Log.wtf("DB_LOG", "ID: " + pContact.contactId()
//                                + "; CONTACT_NAME: " + pContact.contactName()
//                                + "; CONTACT_SURNAME: " + pContact.contactSurname()
//                                + "; CONTACT_NICKNAME: " + pContact.contactNickname()
//                                + "; CONTACT_IS_ONLINE: " + pContact.isOnline()
//                                + "; CONTACT_IMG_URI: " + pContact.contactImgUri());
//                    }
//                });
//    }

}
