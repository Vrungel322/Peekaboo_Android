package com.peekaboo.presentation.presenters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.peekaboo.data.mappers.AbstractMapperFactory;
import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.data.repositories.database.contacts.ContactAbs;
import com.peekaboo.data.repositories.database.contacts.ContactHelper;
import com.peekaboo.domain.ContactsPOJO;
import com.peekaboo.domain.ErrorHandler;
import com.peekaboo.domain.subscribers.BaseProgressSubscriber;
import com.peekaboo.domain.usecase.GetContactsUseCase;
import com.peekaboo.presentation.views.IContactsView;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by Nikita on 11.08.2016.
 */
public class ContactPresenter extends ProgressPresenter<IContactsView> implements IContactPresenter {

    private AbstractMapperFactory mapperFactory;
    private GetContactsUseCase useCase;
    private ContactHelper contactHelper;
    private Context context;

    @Inject
    public ContactPresenter(Context context, AbstractMapperFactory mapperFactory, ContactHelper contactHelper,
                            GetContactsUseCase useCase, ErrorHandler errorHandler) {
        super(context, errorHandler);
        this.context = context;
        this.mapperFactory = mapperFactory;
        this.contactHelper = contactHelper;
        this.useCase = useCase;
    }

    @Override
    public void makeContactsQuery() {
        useCase.execute(getContactsSubscriber());
    }

    @NonNull
    private BaseProgressSubscriber<ContactsPOJO> getContactsSubscriber() {
        return new BaseProgressSubscriber<ContactsPOJO>(this) {
            @Override
            public void onNext(ContactsPOJO response) {
                super.onNext(response);
                Log.e("onNext", String.valueOf(response));
                if (getView() != null) {
                    getView().makeMeNotice();
                }
            }
        };
    }

    @Override
    public void unbind() {
        useCase.unsubscribe();
        super.unbind();
    }

    @Override
    public void bind(IContactsView view) {
        super.bind(view);
        if (useCase.isWorking()) {
            useCase.execute(getContactsSubscriber());
        }
    }

    @Override
    public void createTable(String tableName) {
        contactHelper.createTable(tableName);
    }

    @Override
    public void insertContactToTable(Contact contact) {
        contactHelper.insert(mapperFactory.getPContactMapper().transform(contact));
    }

    @Override
    public void getAllContacts(String tableName, Action1 adapter) {
        //TODO: get all contacts
    }

    @Override
    public void dropTableAndCreate(String tableName) {
        contactHelper.dropTableAndCreate(tableName);
    }

    @Override
    public void getAllTableAsString(String tableName) {
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
