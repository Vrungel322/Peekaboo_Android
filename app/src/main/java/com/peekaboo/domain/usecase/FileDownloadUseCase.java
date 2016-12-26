package com.peekaboo.domain.usecase;

import android.util.Log;

import com.peekaboo.data.di.scope.UserScope;
import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.data.utils.FileUtils;
import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.schedulers.ObserveOn;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Response;

@UserScope
public class FileDownloadUseCase extends QueueUseCase<PMessage, String> {

    private final SessionRepository repository;

    @Inject
    public FileDownloadUseCase(ObserveOn observeOn, SessionRepository repository) {
        super(observeOn);
        this.repository = repository;
    }

    @Override
    protected String getValue(PMessage key, String fileType) throws IOException {
        String remoteFileName = key.messageBody().split(PMessage.DIVIDER)[0];
        Response<ResponseBody> execute
                = repository.downloadFile(remoteFileName, fileType)
                .execute();
        File file = null;
        if (execute.isSuccessful()) {
            Log.wtf("filedownloadUseCase", "start");
            file = FileUtils.writeResponseBodyToDisk(FileUtils.formFileName(key.receiverId(), fileType), execute.body());
            Log.wtf("filedownloadUseCase", "stop");
        }

        if (file == null) return null;
        return file.toString();
    }
}