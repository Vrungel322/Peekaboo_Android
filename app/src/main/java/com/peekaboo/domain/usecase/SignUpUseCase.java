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
public class SignUpUseCase extends UseCase<AccountUser> {
    private SessionRepository sessionRepository;
    private String phone;
    private String username;
    private String email;
    private String password;

    @Inject
    public SignUpUseCase(SessionRepository sessionRepository, SubscribeOn subscribeOn, ObserveOn observeOn) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    public void setCredentials(String phone, String username, String email, String password) {
        this.phone = phone;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    protected Observable<AccountUser> getUseCaseObservable() {
        return sessionRepository.signUp(phone, username, email, password);
    }
}
