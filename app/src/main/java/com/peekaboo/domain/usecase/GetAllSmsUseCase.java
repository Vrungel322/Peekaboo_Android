package com.peekaboo.domain.usecase;

import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.Sms;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by st1ch on 17.10.2016.
 */

public class GetAllSmsUseCase extends UseCase<List<Sms>> {

    private SessionRepository sessionRepository;

    @Inject
    public GetAllSmsUseCase(SessionRepository sessionRepository, SubscribeOn subscribeOn, ObserveOn observeOn) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<List<Sms>> getUseCaseObservable() {
        return sessionRepository.getAllSmsList();
    }
}
