package com.peekaboo.domain.usecase;

import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Nikita on 13.09.2016.
 */
public class FetchContactsUseCase extends UseCase<List<Contact>> {
    private SessionRepository sessionRepository;

    @Inject
    public FetchContactsUseCase(SessionRepository sessionRepository, SubscribeOn subscribeOn, ObserveOn observeOn) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<List<Contact>> getUseCaseObservable() {
        return sessionRepository.loadAllContactsFromDb();
    }
}
