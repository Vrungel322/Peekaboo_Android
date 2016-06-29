package com.peekaboo.data.rest;

import com.peekaboo.data.rest.entity.UserEntity;

import rx.Observable;

/**
 * Created by Arkadiy on 05.06.2016.
 */
public class RestApi {

    private final PeekabooApi api;

    public RestApi(PeekabooApi api) {
        this.api = api;
    }

    public Observable<UserEntity> login(String email, String password) {
        return api.login(email, password);
    }
}
