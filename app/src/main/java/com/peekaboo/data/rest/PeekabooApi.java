package com.peekaboo.data.rest;

import com.peekaboo.data.rest.entity.Credentials;
import com.peekaboo.data.rest.entity.TokenEntity;

import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/*
 * Created by Arkadiy on 01.06.2016.
 */
public interface PeekabooApi {

    String SIGNIN = "signin";
    String SIGNUP = "signup";
    String CONFIRM = "confirm";
    String GET_KEY = "";

    @FormUrlEncoded
    @POST(SIGNIN)
    Observable<TokenEntity> login(
            @Body Credentials credentials
    );

    @POST(SIGNUP)
    Observable<TokenEntity> signUp(
            @Body Credentials credentials
    );

    @POST(CONFIRM)
    Observable<TokenEntity> confirm(
            @Body ConfirmKey confirmKey
    );

}
