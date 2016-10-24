package com.peekaboo.domain.usecase;

import android.support.annotation.Nullable;

import com.peekaboo.data.FileEntity;
import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.schedulers.ObserveOn;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Response;

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
    protected FileEntity getValue(PMessage take, String fileType) throws IOException {
        Response<FileEntity> execute =
                repository.uploadFile(fileType, take.messageBody(), take.receiverId())
                        .execute();
        if (execute.isSuccessful()) {
            return execute.body();
        }
        return null;
    }
}