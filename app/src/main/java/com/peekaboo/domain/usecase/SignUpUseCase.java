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
public class SignUpUseCase extends UseCase<User> {
    private SessionRepository sessionRepository;
    private String login;
    private String password;
    private String email;

    @Inject
    public SignUpUseCase(SessionRepository sessionRepository, SubscribeOn subscribeOn, ObserveOn observeOn) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    public void setCredentials(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }

    @Override
    protected Observable<User> getUseCaseObservable() {
        return sessionRepository.signUp(login, password, email);
    }
}
