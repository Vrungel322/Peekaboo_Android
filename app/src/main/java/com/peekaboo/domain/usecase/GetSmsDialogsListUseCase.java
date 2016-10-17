package com.peekaboo.domain.usecase;

import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.SmsDialog;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by st1ch on 17.10.2016.
 */

public class GetSmsDialogsListUseCase extends UseCase<List<SmsDialog>> {

    private SessionRepository sessionRepository;

    @Inject
    public GetSmsDialogsListUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<List<SmsDialog>> getUseCaseObservable() {
        return sessionRepository.getSmsDialogsList();
    }
}
