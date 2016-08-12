package com.peekaboo.domain.usecase;

import com.peekaboo.data.FileEntity;
import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;

import java.io.File;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by sebastian on 12.08.16.
 */
public class DownloadFileUseCase extends UseCase<File> {

    private SessionRepository repository;
    private String fileName;
    private String remoteFileName;

    @Inject
    public DownloadFileUseCase(SessionRepository repository, SubscribeOn subscribeOn, ObserveOn observeOn) {
        super(subscribeOn, observeOn);
        this.repository = repository;
    }

    public void setInfo(String fileName, String remoteFileName) {
        this.fileName = fileName;
        this.remoteFileName = remoteFileName;
    }

    @Override
    protected Observable<File> getUseCaseObservable() {
        return repository.downloadFile(fileName, remoteFileName);
    }
}
