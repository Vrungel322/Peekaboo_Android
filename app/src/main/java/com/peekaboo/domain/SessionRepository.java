package com.peekaboo.domain;


import com.peekaboo.data.FileEntity;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;
import rx.Observable;

/**
 * Created by Arkadiy on 05.06.2016.
 */
public interface SessionRepository {
    Observable<AccountUser> login(String login, String password);
    Observable<AccountUser> signUp(String username, String login, String password);
    Observable<AccountUser> confirm(String id, String key);
    Observable<User> findFriendByName(String friendName);

    Call<FileEntity> uploadFile(String fileName, String receiverId);

    Call<ResponseBody> downloadFile(String remoteFileName);
}
