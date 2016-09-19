package com.peekaboo.data.rest;

import com.peekaboo.data.FileEntity;
import com.peekaboo.data.rest.entity.ContactEntity;
import com.peekaboo.data.rest.entity.Credentials;
import com.peekaboo.data.rest.entity.CredentialsSignUp;
import com.peekaboo.data.rest.entity.TokenEntity;
import com.peekaboo.data.rest.entity.UserEntity;
import com.peekaboo.data.rest.entity.UserResponse;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import rx.Observable;

/**
 * Created by Arkadiy on 05.06.2016.
 */
public class RestApi {

    private final PeekabooApi api;

    public RestApi(PeekabooApi api) {
        this.api = api;
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

    public Observable<UserEntity> findFriendByName(String friendName) {
        return api.getFriend(friendName);
    }

    public Call<FileEntity> uploadFile(String fileName, String receiverId, String bearer) {
        File file = new File(fileName);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        return api.uploadFile(receiverId, part, bearer);
    }

    public Call<ResponseBody> downloadFile(String remoteFileName, String bearer) {
        return api.download(remoteFileName, bearer);
    }

    public Observable<UserResponse> getAllContacts() {
        return api.getAllContacts();
    }
}
