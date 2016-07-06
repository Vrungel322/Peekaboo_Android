package com.peekaboo.data.rest;

import com.peekaboo.data.rest.entity.Credentials;
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

    public Observable<TokenEntity> login(Credentials credentials) {
        return api.login(credentials);
    }

    public Observable<TokenEntity> signUp(Credentials credentials) {
        return api.signUp(credentials);
    }

    public Observable<TokenEntity> confirm(ConfirmKey confirmKey) {
        return api.confirm(confirmKey);
    }
}
