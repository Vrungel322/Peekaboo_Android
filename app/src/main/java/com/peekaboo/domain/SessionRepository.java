package com.peekaboo.domain;


import com.peekaboo.data.FileEntity;

import java.io.File;

import rx.Observable;

/**
 * Created by Arkadiy on 05.06.2016.
 */
public interface SessionRepository {
    Observable<AccountUser> login(String login, String password);
    Observable<AccountUser> signUp(String username, String login, String password);
    Observable<AccountUser> confirm(String id, String key);
    Observable<User> findFriendByName(String friendName);

    Observable<FileEntity> uploadFile(String fileName, String receiverId);

    Observable<File> downloadFile(String fileName, String remoteFileName);
}
