package com.peekaboo.domain.usecase;

import android.support.annotation.Nullable;

import com.peekaboo.data.Constants;
import com.peekaboo.data.FileEntity;
import com.peekaboo.data.di.scope.UserScope;
import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.data.repositories.database.messages.PMessageAbs;
import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.utils.FilesUtils;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import retrofit2.Response;

@UserScope
public class FileUploadUseCase extends QueueUseCase<PMessage, FileEntity> {

    private final SessionRepository repository;
    private FilesUtils filesUtils;

    @Inject
    public FileUploadUseCase(ObserveOn observeOn, SessionRepository repository, FilesUtils filesUtils) {
        super(observeOn);
        this.repository = repository;
        this.filesUtils = filesUtils;
    }

    @Nullable
    @Override
    protected FileEntity getValue(PMessage take, String fileType) throws IOException {
        File uploadableImageFile = null;

        String fileName = take.messageBody();
        if (take.mediaType() == PMessageAbs.PMESSAGE_MEDIA_TYPE.IMAGE_MESSAGE) {
            uploadableImageFile = filesUtils.createUploadableImageFile(fileName, Constants.IMAGE_SIZES.IMAGE_SIZE);
            fileName = uploadableImageFile.getAbsolutePath();
        }
        Response<FileEntity> execute =
                repository.uploadFile(fileType, fileName, take.receiverId())
                        .execute();
        if (take.mediaType() == PMessageAbs.PMESSAGE_MEDIA_TYPE.IMAGE_MESSAGE &&
                uploadableImageFile != null) {
            FilesUtils.deleteFile(uploadableImageFile);
        }

        if (execute.isSuccessful()) {
            return execute.body();
        }
        return null;
    }
}