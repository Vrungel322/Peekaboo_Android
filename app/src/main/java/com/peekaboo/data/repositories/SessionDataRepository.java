package com.peekaboo.data.repositories;

import com.peekaboo.data.FileEntity;
import com.peekaboo.data.mappers.AbstractMapperFactory;
import com.peekaboo.data.rest.ConfirmKey;
import com.peekaboo.data.rest.RestApi;
import com.peekaboo.data.rest.entity.Credentials;
import com.peekaboo.data.rest.entity.CredentialsSignUp;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.domain.ContactsPOJO;
import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.User;

import java.io.File;

import rx.Observable;

/**
 * Created by Arkadiy on 05.06.2016.
 */
public class SessionDataRepository implements SessionRepository {

    private final AbstractMapperFactory abstractMapperFactory;
    private AccountUser user;
    private RestApi restApi;

    public SessionDataRepository(RestApi restApi, AbstractMapperFactory abstractMapperFactory, AccountUser user) {
        this.restApi = restApi;
        this.abstractMapperFactory = abstractMapperFactory;
        this.user = user;
    }

    @Override
    public Observable<AccountUser> login(String login, String password) {
        return restApi.login(new Credentials(login, password))
                .map(token -> {
                    user.saveToken(token.getToken());
                    user.saveId(token.getId());
                    return user;
                });
    }

    @Override
    public Observable<AccountUser> signUp(String username, String login, String password) {
        return restApi.signUp(new CredentialsSignUp(username, login, password))
                .map(token -> {
                    user.saveId(token.getId());
                    return user;
                });
    }

    @Override
    public Observable<AccountUser> confirm(String id, String key) {
        return restApi.confirm(new ConfirmKey(id, key))
                .map(token -> {
                    user.saveToken(token.getToken());
                    return user;
                });
    }

    @Override
    public Observable<User> findFriendByName(String friendName) {
        return restApi.findFriendByName(friendName)
                .map(user -> abstractMapperFactory.getUserMapper().transform(user));
    }

    @Override
    public Observable<FileEntity> uploadFile(String fileName, String receiverId) {
        return restApi.uploadFile(fileName, receiverId, user.getBearer());
    }

    @Override
    public Observable<File> downloadFile(String fileName, String remoteFileName) {
        return restApi.downloadFile(fileName, remoteFileName, user.getBearer());
    }

    @Override
    public Observable<ContactsPOJO> loadAllContacts() {
        return restApi.loadAllContacts();
    }
}