package com.peekaboo.data.repositories;

import com.peekaboo.data.mappers.AbstractMapperFactory;
import com.peekaboo.data.mappers.Mapper;
import com.peekaboo.data.rest.RestApi;
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
    public Observable<User> askForUser(String username, String password) {
        return restApi.login(username, password).map(token -> {
            user.saveBearer(token);
            return user;
        });
    }

    @Override
    public Observable<User> signUp(String login, String password, String email) {
        return restApi.signUp(login, password, email).map(token -> {
            user.saveBearer(token);
            return user;
        });
    }
}