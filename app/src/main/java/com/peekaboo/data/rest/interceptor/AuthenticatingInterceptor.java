package com.peekaboo.data.rest.interceptor;

import android.util.Log;

import com.peekaboo.data.Constants;
import com.peekaboo.data.encryption.Authentificator;
import com.peekaboo.data.rest.PeekabooApi;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sebastian on 30.06.16.
 */
public class AuthenticatingInterceptor implements Interceptor {
    private Authentificator authentificator;

    public AuthenticatingInterceptor(Authentificator authentificator) {
        this.authentificator = authentificator;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Log.e("response", String.valueOf(authentificator.hasPublicKey()));
        if (!authentificator.hasPublicKey()) {
            Request getKey = new Request.Builder().url(Constants.BASE_URL + PeekabooApi.GET_KEY).build();
            Response keyResponse = chain.proceed(getKey);
            Log.e("response", String.valueOf(keyResponse));
            if (keyResponse.isSuccessful()) {
                String publicKey = keyResponse.body().string();
                keyResponse.body().close();
                authentificator.savePublicKey(publicKey);
                Log.e("key", String.valueOf(publicKey));
            }
        }
        if (authentificator.hasPublicKey()) {
            switch (original.method()) {
                case PeekabooApi.SIGNIN:
                case PeekabooApi.SIGNUP:
                    return chain.proceed(original);
                default:
                    return chain.proceed(original.newBuilder()
//                            .addHeader("Authentication", )
                            .method(original.method(), original.body())
                            .build());
            }
        } else {
            throw new RuntimeException("public key was not obtained");
        }

    }
}
