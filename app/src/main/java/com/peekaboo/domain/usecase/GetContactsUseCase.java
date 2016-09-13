package com.peekaboo.domain.usecase;

import com.peekaboo.domain.ContactsPOJO;
import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Nikita on 13.09.2016.
 */
public class GetContactsUseCase extends UseCase<ContactsPOJO> {
    private SessionRepository sessionRepository;

    @Inject
    public GetContactsUseCase(SessionRepository sessionRepository, SubscribeOn subscribeOn, ObserveOn observeOn) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<ContactsPOJO> getUseCaseObservable() {
        return sessionRepository.loadAllContacts();
    }
}
