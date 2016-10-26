package com.peekaboo.domain.usecase;

import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.SmsDialog;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by st1ch on 24.10.2016.
 */

public class GetSmsDialogsUseCase extends UseCase<SmsDialog> {

    private SessionRepository sessionRepository;

    @Inject
    public GetSmsDialogsUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<SmsDialog> getUseCaseObservable() {
        return sessionRepository.getSmsDialogs();
    }
}
