package com.peekaboo.domain.usecase;

import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.User;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by sebastian on 28.06.16.
 */
public class ConfirmUseCase extends UseCase<User> {
    private SessionRepository sessionRepository;
    private String id;
    private String key;

    @Inject
    public ConfirmUseCase(SessionRepository sessionRepository, SubscribeOn subscribeOn, ObserveOn observeOn) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    public void setConfirmData(String id, String key) {
        this.id = id;
        this.key = key;
    }

    @Override
    protected Observable<User> getUseCaseObservable() {
        return sessionRepository.confirm(id, key);
    }
}
