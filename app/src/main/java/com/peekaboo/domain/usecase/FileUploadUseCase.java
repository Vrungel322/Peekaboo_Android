package com.peekaboo.domain.usecase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.peekaboo.data.FileEntity;
import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.domain.Pair;
import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

@Singleton
public class FileUploadUseCase extends QueueUseCase<PMessage, FileEntity> {

    private final SessionRepository repository;

    @Inject
    public FileUploadUseCase(ObserveOn observeOn, SessionRepository repository) {
        super(observeOn);
        this.repository = repository;
    }

    @Nullable
    @Override
    protected FileEntity getValue(PMessage take) throws IOException {
        Response<FileEntity> execute = repository.uploadFile(take.messageBody(), take.receiverId()).execute();
        if (execute.isSuccessful()) {
            return execute.body();
        }
        return null;
    }
}