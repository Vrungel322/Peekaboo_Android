package com.peekaboo.domain.usecase;

import com.peekaboo.data.di.scope.UserScope;
import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by arkadius on 10/5/16.
 */
@UserScope
public class GetContactByContactIdUseCase extends UseCase<Contact> {

    private final SessionRepository sessionRepository;
    private String contactId;

    @Inject
    public GetContactByContactIdUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    @Override
    protected Observable<Contact> getUseCaseObservable() {
        return sessionRepository.getContactByContactId(contactId);
    }
}
