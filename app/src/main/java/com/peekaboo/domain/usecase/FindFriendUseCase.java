package com.peekaboo.domain.usecase;

import com.peekaboo.domain.AccountUser;
import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.User;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by sebastian on 12.08.16.
 */
public class FindFriendUseCase extends UseCase<User> {

    private SessionRepository repository;
    private String friendName;

    @Inject
    public FindFriendUseCase(SessionRepository repository, SubscribeOn subscribeOn, ObserveOn observeOn) {
        super(subscribeOn, observeOn);
        this.repository = repository;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    @Override
    protected Observable<User> getUseCaseObservable() {
        return repository.findFriendByName(friendName);
    }
}
