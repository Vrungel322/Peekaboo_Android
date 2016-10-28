package com.peekaboo.domain.usecase;

import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.Sms;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by st1ch on 21.10.2016.
 */

public class GetLastSmsUseCase extends UseCase<Sms> {
    private SessionRepository sessionRepository;
    private String phoneNumber;

    @Inject
    public GetLastSmsUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    protected Observable<Sms> getUseCaseObservable() {
        return sessionRepository.getContactLastSms(phoneNumber);
    }
}
