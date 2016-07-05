package com.peekaboo.data;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.peekaboo.domain.Error;
import com.peekaboo.domain.ErrorHandler;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by sebastian on 05.07.16.
 */
@Singleton
public class ResponseErrorHandler implements ErrorHandler {
    private Gson gson = new Gson();
    private Context context;

    @Inject
    public ResponseErrorHandler(Context context) {
        this.context = context;
    }

    @Override
    public String handleError(Throwable t) {

        if (t instanceof HttpException) {
            ResponseBody body = ((HttpException) t).response().errorBody();
            TypeAdapter<Error> adapter = gson.getAdapter(Error.class);
            try {
                Error error = adapter.fromJson(body.string());
                return error.getMessage();
            } catch (IOException e) {}
        }
        return t.getMessage();
    }
}
