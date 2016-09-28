package com.peekaboo.domain.usecase;

import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by sebastian on 28.06.16.
 */
public class LoginUseCase extends UseCase<AccountUser> {
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
    protected Observable<AccountUser> getUseCaseObservable() {
        return sessionRepository.login(login, password);
    }
}
