package com.peekaboo.data.rest;

import android.content.Context;

import com.peekaboo.data.Constants;
import com.peekaboo.data.FileEntity;
import com.peekaboo.data.rest.entity.Credentials;
import com.peekaboo.data.rest.entity.CredentialsSignUp;
import com.peekaboo.data.rest.entity.TokenEntity;
import com.peekaboo.data.rest.entity.UserEntity;
import com.peekaboo.data.rest.entity.UserResponse;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.utils.FilesUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Arkadiy on 05.06.2016.
 */
public class RestApi {

    private final PeekabooApi api;
    private Context c;
    private FilesUtils filesUtils;

    public RestApi(PeekabooApi api, Context c, FilesUtils filesUtils) {
        this.api = api;
        this.c = c;
        this.filesUtils = filesUtils;
    }

    public Observable<TokenEntity> login(Credentials credentials) {
        return api.login(credentials);
    }

    public Observable<TokenEntity> signUp(CredentialsSignUp credentials) {
        return api.signUp(credentials);
    }

    public Observable<TokenEntity> confirm(ConfirmKey confirmKey) {
        return api.confirm(confirmKey);
    }

    public Observable<ResponseBody> updateAccountData(AccountUser accountUser){
        return api.updateAccountData(accountUser.getId(), accountUser);
    }

    public Observable<UserEntity> findFriendByName(String friendName) {
        return api.getFriend(friendName);
    }

    public Call<FileEntity> uploadFile(String fileType, String fileName, String receiverId, String bearer) {
        File file = new File(fileName);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        return api.uploadFile(fileType, receiverId, part, bearer);
    }

    public Observable<FileEntity> updateAvatar(String fileName, String bearer){
        return Observable.create(new Observable.OnSubscribe<MultipartBody.Part>() {
            @Override
            public void call(Subscriber<? super MultipartBody.Part> subscriber) {
                try {
                    File tmpFile = filesUtils.createUploadableImageFile(fileName, Constants.IMAGE_SIZES.AVATAR_SIZE);
                    RequestBody requestFile =
                            RequestBody.create(MediaType.parse("multipart/form-data"), tmpFile);
                    MultipartBody.Part part = MultipartBody.Part.createFormData("image", tmpFile.getName(), requestFile);

                    subscriber.onNext(part);
                    FilesUtils.deleteFile(tmpFile);
                    subscriber.onCompleted();

                } catch (IOException e) {
                    subscriber.onError(new Throwable("file loading error"));
                }
            }
        }).flatMap(part -> api.updateAvatar(part, bearer));
    }

    public Call<ResponseBody> downloadFile(String fileType, String remoteFileName, String bearer) {
        return api.download(fileType, remoteFileName, bearer);
    }

    public Observable<UserResponse> getAllContacts() {
        return api.getAllContacts();
    }
}
