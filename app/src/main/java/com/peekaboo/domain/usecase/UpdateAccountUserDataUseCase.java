package com.peekaboo.domain.usecase;

import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.User;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by Nikita on 28.11.2016.
 */

public class UpdateAccountUserDataUseCase extends UseCase<ResponseBody> {

    private final SessionRepository sessionRepository;
    private User user;

    @Inject
    public UpdateAccountUserDataUseCase(SessionRepository sessionRepository, SubscribeOn subscribeOn, ObserveOn observeOn) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    public void setCredentials(User accountUser) {
      this.user = accountUser;
    }

    @Override
    protected Observable<ResponseBody> getUseCaseObservable() {
        return sessionRepository.updateAccountData(user);
    }
}
