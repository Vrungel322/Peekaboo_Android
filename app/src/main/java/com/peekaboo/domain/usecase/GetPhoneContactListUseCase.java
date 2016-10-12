package com.peekaboo.domain.usecase;

import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;
import com.peekaboo.presentation.pojo.PhoneContactPOJO;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Nikita on 10.10.2016.
 */
public class GetPhoneContactListUseCase extends UseCase<List<PhoneContactPOJO>> {
    private SessionRepository sessionRepository;

    @Inject
    public GetPhoneContactListUseCase(SubscribeOn subscribeOn,
                                 ObserveOn observeOn,
                                 SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<List<PhoneContactPOJO>> getUseCaseObservable() {
        return sessionRepository.getPhoneContactList();
    }
}
