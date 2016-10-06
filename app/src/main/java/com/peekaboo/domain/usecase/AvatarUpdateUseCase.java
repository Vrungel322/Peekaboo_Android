package com.peekaboo.domain.usecase;

import com.peekaboo.data.FileEntity;
import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Nikita on 06.10.2016.
 */
public class AvatarUpdateUseCase extends UseCase<FileEntity> {
    private SessionRepository sessionRepository;
    private String fileName;
    private String bearer;

    @Inject
    public AvatarUpdateUseCase(SessionRepository sessionRepository, SubscribeOn subscribeOn, ObserveOn observeOn) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    public void setDataForUpdatingAvatar(String fileName, String bearer){
        this.fileName = fileName;
        this.bearer = bearer;
    }

    @Override
    protected Observable<FileEntity> getUseCaseObservable() {
        return sessionRepository.updateAvatar(fileName, bearer);
    }
}
