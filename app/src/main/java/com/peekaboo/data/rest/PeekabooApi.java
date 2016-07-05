package com.peekaboo.data.rest;

import com.peekaboo.data.rest.entity.TokenEntity;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/*
 * Created by Arkadiy on 01.06.2016.
 */
public interface PeekabooApi {

    String SIGNIN = "signin";
    String SIGNUP = "signup";
    String GET_KEY = "";

    @FormUrlEncoded
    @POST(SIGNIN)
    Observable<String> login(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST(SIGNUP)
    Observable<TokenEntity> signUp(
            @Field("username") String username,
            @Field("password") String password,
            @Field("email") String email
    );

}
