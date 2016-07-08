package com.peekaboo.data.repositories;

import com.peekaboo.data.mappers.AbstractMapperFactory;
import com.peekaboo.data.rest.ConfirmKey;
import com.peekaboo.data.rest.RestApi;
import com.peekaboo.data.rest.entity.Credentials;
import com.peekaboo.data.rest.entity.CredentialsSignUp;
import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.User;

import rx.Observable;

/**
 * Created by Arkadiy on 05.06.2016.
 */
public class SessionDataRepository implements SessionRepository {

    private final AbstractMapperFactory abstractMapperFactory;
    private User user;
    private RestApi restApi;

    public SessionDataRepository(RestApi restApi, AbstractMapperFactory abstractMapperFactory, User user) {
        this.restApi = restApi;
        this.abstractMapperFactory = abstractMapperFactory;
        this.user = user;
    }

    @Override
    public Observable<User> login( String login, String password) {
        return restApi.login(new Credentials(login, password)).map(token -> {
            user.saveToken(token.getToken());
            user.saveId(token.getId());
            return user;
        });
    }

    @Override
    public Observable<User> signUp(String username, String login, String password) {
        return restApi.signUp(new CredentialsSignUp(username, login, password)).map(token -> {
            user.saveId(token.getId());
            return user;
        });
    }

    @Override
    public Observable<User> confirm(String id, String key) {
        return restApi.confirm(new ConfirmKey(id, key)).map(token -> {
            user.saveToken(token.getToken());
            user.saveId(token.getId());
            return user;
        });
    }
}