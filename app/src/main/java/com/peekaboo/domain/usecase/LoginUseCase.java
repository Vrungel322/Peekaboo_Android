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
public class LoginUseCase extends UseCase<User> {
    private SessionRepository sessionRepository;
    private String login;
    private String password;

    @Inject
    public LoginUseCase(SessionRepository sessionRepository, SubscribeOn subscribeOn, ObserveOn observeOn) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    public void setCredentials(String login, String password) {
        this.login = login;
        this.password = password;
    }
    @Override
    protected Observable<User> getUseCaseObservable() {
        return sessionRepository.askForUser(login, password);
    }
}
