package com.peekaboo.domain.usecase;

import com.peekaboo.domain.AccountUser;
import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by sebastian on 28.06.16.
 */
public class ConfirmUseCase extends UseCase<AccountUser> {
    private SessionRepository sessionRepository;
    private String id;
    private String key;

    @Inject
    public ConfirmUseCase(SessionRepository sessionRepository, SubscribeOn subscribeOn, ObserveOn observeOn) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    public void setConfirmKey(String key) {
        this.key = key;
    }

    @Override
    protected Observable<AccountUser> getUseCaseObservable() {
        return sessionRepository.confirm(id, key);
    }

    public void setUserId(String id) {
        this.id = id;
    }
}
