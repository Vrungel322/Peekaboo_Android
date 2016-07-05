package com.peekaboo.data.rest;

import com.peekaboo.data.rest.entity.TokenEntity;
import com.peekaboo.data.rest.entity.UserEntity;
import com.peekaboo.domain.User;

import rx.Observable;

/**
 * Created by Arkadiy on 05.06.2016.
 */
public class RestApi {

    private final PeekabooApi api;

    public RestApi(PeekabooApi api) {
        this.api = api;
    }

    public Observable<String> login(String username, String password) {
        return api.login(username, password);
    }

    public Observable<TokenEntity> signUp(String login, String password, String email) {
        return api.signUp(login, password, email);
    }
}
