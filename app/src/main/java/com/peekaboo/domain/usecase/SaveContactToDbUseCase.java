package com.peekaboo.domain.usecase;

import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Nikita on 21.09.2016.
 */
public class SaveContactToDbUseCase extends UseCase<List<Contact>> {
    private SessionRepository sessionRepository;
    private List<Contact> contactList;

    @Inject
    public SaveContactToDbUseCase(SessionRepository sessionRepository, SubscribeOn subscribeOn, ObserveOn observeOn) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }


    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }


    @Override
    protected Observable getUseCaseObservable() {
        return sessionRepository.saveContactToDb(contactList);
    }
}
