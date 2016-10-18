package com.peekaboo.data.rest;

import com.peekaboo.data.FileEntity;
import com.peekaboo.data.rest.entity.Credentials;
import com.peekaboo.data.rest.entity.CredentialsSignUp;
import com.peekaboo.data.rest.entity.TokenEntity;
import com.peekaboo.data.rest.entity.UserEntity;
import com.peekaboo.data.rest.entity.UserResponse;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
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
    Observable<UserEntity> getFriend(
            @Query("username") String username
    );

    @Multipart
    @POST("upload/audio/{id}")
    Call<FileEntity> uploadFile(
            @Path("id") String receiverId,
            @Part MultipartBody.Part body,
            @Header("authorization") String bearer
    );

    @Multipart
    @POST("avatar")
    Observable<FileEntity> updateAvatar(
            @Part MultipartBody.Part body,
            @Header("authorization") String bearer
    );

    @GET("download/{fileType}/{fileName}")
    Call<ResponseBody> download(
            @Path("fileType") String fileType,
            @Path("fileName") String fileName,
            @Header("authorization") String bearer
    );

    @GET("allusers/find")
    Observable<UserResponse> getAllContacts();
}
