package com.peekaboo.data.rest;

import com.peekaboo.data.rest.entity.Credentials;
import com.peekaboo.data.rest.entity.CredentialsSignUp;
import com.peekaboo.data.rest.entity.TokenEntity;
import com.peekaboo.data.rest.entity.UserEntity;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/*
 * Created by Arkadiy on 01.06.2016.
 */
public interface PeekabooApi {

    String SIGNIN = "signin";
    String SIGNUP = "signup";
    String CONFIRM = "confirm";
    String GET_KEY = "";

    @POST(SIGNIN)
    Observable<TokenEntity> login(
            @Body Credentials credentials
    );

    @POST(SIGNUP)
    Observable<TokenEntity> signUp(
            @Body CredentialsSignUp credentials
    );

    @POST(CONFIRM)
    Observable<TokenEntity> confirm(
            @Body ConfirmKey confirmKey
    );

    @GET("friend/find")
    Call<UserEntity> getFriend(
            @Query("username") String username
    );

}
