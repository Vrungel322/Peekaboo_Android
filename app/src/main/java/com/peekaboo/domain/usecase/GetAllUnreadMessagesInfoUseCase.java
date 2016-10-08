package com.peekaboo.domain.usecase;

import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.domain.Pair;
import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by arkadius on 10/5/16.
 */
@Singleton
public class GetAllUnreadMessagesInfoUseCase extends UseCase<Pair<List<PMessage>, List<Contact>>> {

    private final SessionRepository sessionRepository;

    @Inject
    public GetAllUnreadMessagesInfoUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<Pair<List<PMessage>, List<Contact>>> getUseCaseObservable() {
        return sessionRepository.getAllUnreadMessagesInfo();
    }
}
