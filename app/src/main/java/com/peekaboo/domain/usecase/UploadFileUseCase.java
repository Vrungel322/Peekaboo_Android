package com.peekaboo.domain.usecase;

import com.peekaboo.data.FileEntity;
import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.User;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by sebastian on 12.08.16.
 */
public class UploadFileUseCase extends UseCase<FileEntity> {

    private SessionRepository repository;
    private String fileName;
    private String receiverId;

    @Inject
    public UploadFileUseCase(SessionRepository repository, SubscribeOn subscribeOn, ObserveOn observeOn) {
        super(subscribeOn, observeOn);
        this.repository = repository;
    }

    public void setInfo(String fileName, String receiverId) {
        this.fileName = fileName;
        this.receiverId = receiverId;
    }

    @Override
    protected Observable<FileEntity> getUseCaseObservable() {
        return repository.uploadFile(fileName, receiverId);
    }
}
