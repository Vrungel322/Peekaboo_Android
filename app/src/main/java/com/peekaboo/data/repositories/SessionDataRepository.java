package com.peekaboo.data.repositories;

import com.peekaboo.data.mappers.AbstractMapperFactory;
import com.peekaboo.data.mappers.Mapper;
import com.peekaboo.data.rest.RestApi;
import com.peekaboo.data.rest.entity.UserEntity;
import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.User;

import rx.Observable;

/**
 * Created by Arkadiy on 05.06.2016.
 */
public class SessionDataRepository implements SessionRepository {

    private final AbstractMapperFactory abstractMapperFactory;
    private RestApi restApi;

    public SessionDataRepository(RestApi restApi, AbstractMapperFactory abstractMapperFactory) {
        this.restApi = restApi;
        this.abstractMapperFactory = abstractMapperFactory;
    }

    @Override
    public Observable<User> askForUser(String login, String password) {
        Mapper<UserEntity, User> loginMapper = abstractMapperFactory.getUserMapper();
        return restApi.login(login, password).map(loginMapper::transform);
    }
}
