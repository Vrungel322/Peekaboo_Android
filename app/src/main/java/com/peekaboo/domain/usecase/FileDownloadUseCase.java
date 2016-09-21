package com.peekaboo.domain.usecase;

import android.support.annotation.Nullable;

import com.peekaboo.data.FileEntity;
import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.data.utils.FileUtils;
import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.schedulers.ObserveOn;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.ResponseBody;
import retrofit2.Response;

@Singleton
public class FileDownloadUseCase extends QueueUseCase<PMessage, String> {

    private final SessionRepository repository;

    @Inject
    public FileDownloadUseCase(ObserveOn observeOn, SessionRepository repository) {
        super(observeOn);
        this.repository = repository;
    }

    @Override
    protected String getValue(PMessage key) throws IOException {
        String remoteFileName = key.messageBody().split(PMessage.DIVIDER)[0];
        Response<ResponseBody> execute = repository.downloadFile(remoteFileName).execute();
        File file = null;
        if (execute.isSuccessful()) {
            file = FileUtils.writeResponseBodyToDisk(FileUtils.formAudioName(key.receiverId()), execute.body());
        }

        if (file == null) return null;
        return file.toString();
    }
}