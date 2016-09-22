package com.peekaboo.presentation.presenters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.peekaboo.data.mappers.AbstractMapperFactory;
import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.data.repositories.database.contacts.ContactAbs;
import com.peekaboo.data.repositories.database.contacts.PContactHelper;
import com.peekaboo.domain.UserMessageMapper;
import com.peekaboo.domain.subscribers.BaseProgressSubscriber;
import com.peekaboo.domain.subscribers.BaseUseCaseSubscriber;
import com.peekaboo.domain.usecase.FetchContactsUseCase;
import com.peekaboo.domain.usecase.GetContactFromDbUseCase;
import com.peekaboo.domain.usecase.SaveContactToDbUseCase;
import com.peekaboo.presentation.views.IContactsView;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by Nikita on 11.08.2016.
 */
public class ContactPresenter extends ProgressPresenter<IContactsView> implements IContactPresenter {

    private FetchContactsUseCase fetchUseCase;
    private PContactHelper contactHelper;
    private Context context;
    private AbstractMapperFactory mapperFactory;
    private PContactHelper pContactHelper;
    private GetContactFromDbUseCase getContactFromDbUseCase;
    private SaveContactToDbUseCase saveContactToDbUseCase;

    @Inject
    public ContactPresenter(Context context, AbstractMapperFactory mapperFactory, PContactHelper contactHelper,
                            FetchContactsUseCase fetchUseCase, UserMessageMapper errorHandler,
                            GetContactFromDbUseCase getContactFromDbUseCase,
                            SaveContactToDbUseCase saveContactToDbUseCase) {
        super(errorHandler);
        this.context = context;
        this.mapperFactory = mapperFactory;
        this.contactHelper = contactHelper;
        this.fetchUseCase = fetchUseCase;
        this.getContactFromDbUseCase = getContactFromDbUseCase;
        this.saveContactToDbUseCase = saveContactToDbUseCase;
    }

    @Override
    public void bind(IContactsView view) {
        super.bind(view);
        if (getContactFromDbUseCase.isWorking()) {
            getContactFromDbUseCase.execute(getContactsSubscriber());
        }
        if (saveContactToDbUseCase.isWorking()) {
            saveContactToDbUseCase.execute(getContactsSubscriber());
        }
        if (fetchUseCase.isWorking()) {
            fetchUseCase.execute(getContactsSubscriber());
        }
    }

    @Override
    public void unbind() {
        saveContactToDbUseCase.unsubscribe();
        getContactFromDbUseCase.unsubscribe();
        fetchUseCase.unsubscribe();
        super.unbind();
    }

    @Override
    public void loadContactsList() {
        fetchUseCase.execute(getContactsSubscriber());
    }

    @NonNull
    private BaseProgressSubscriber<List<Contact>> getContactsSubscriber() {
        return new BaseProgressSubscriber<List<Contact>>(this) {
            @Override
            public void onNext(List<Contact> response) {
                super.onNext(response);
                Log.e("onNext", String.valueOf(response.get(1).contactName()));
                saveContactToDbUseCase.setContact(response);
                saveContactToDbUseCase.execute(saveContactToDb());
//                getContactFromDbUseCase.execute(getContactsFromDb());
                getAllTableAsString();
            }
        };
    }

    private Subscriber<List<Contact>> saveContactToDb() {
        return new BaseUseCaseSubscriber<List<Contact>>() {
            @Override
            public void onNext(List<Contact> contacts) {
                super.onNext(contacts);
                getContactFromDbUseCase.execute(getContactsFromDb());
            }
        };
    }

    public Subscriber<List<Contact>> getContactsFromDb() {
        return new BaseUseCaseSubscriber<List<Contact>>() {
            @Override
            public void onNext(List<Contact> contacts) {
                super.onNext(contacts);
                IContactsView view = getView();
                if (view != null) {
                    view.showContactsList(contacts);
                }
            }
        };
    }

    @Override
    public void insertContactToTable(Contact contact) {
        contactHelper.insert(contact);
    }

    @Override
    public void getAllContacts(String tableName, Action1 adapter) {
        //TODO: get all contacts
        contactHelper.getAllContacts();
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
