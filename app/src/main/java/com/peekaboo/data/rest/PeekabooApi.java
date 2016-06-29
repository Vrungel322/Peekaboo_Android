package com.peekaboo.data.rest;

import com.peekaboo.data.rest.entity.UserEntity;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/*
 * Created by Arkadiy on 01.06.2016.
 */
public interface PeekabooApi {

    @FormUrlEncoded
    @POST("signin")
    Observable<UserEntity> login(
            @Field("username") String email,
            @Field("password") String password);
}
