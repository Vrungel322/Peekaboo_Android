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
import com.peekaboo.domain.usecase.GetContactsUseCase;
import com.peekaboo.presentation.views.IContactsView;

import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by Nikita on 11.08.2016.
 */
public class ContactPresenter extends ProgressPresenter<IContactsView> implements IContactPresenter {

    private GetContactsUseCase useCase;

    @Inject
    public ContactPresenter(GetContactsUseCase useCase, UserMessageMapper errorHandler) {
        super(errorHandler);
        this.useCase = useCase;
    }

    @Override
    public void bind(IContactsView view) {
        super.bind(view);
        if (!useCase.isWorking()) {
            useCase.execute(getContactsSubscriber());
        }
    }

    @Override
    public void unbind() {
        useCase.unsubscribe();
        super.unbind();
    }

    @NonNull
    private BaseProgressSubscriber<List<Contact>> getContactsSubscriber() {
        return new BaseProgressSubscriber<List<Contact>>(this) {
            @Override
            public void onNext(List<Contact> response) {
                super.onNext(response);
                IContactsView view = getView();
                if (view != null) {
                    view.showContactsList();
                }
            }
        };
    }
}
