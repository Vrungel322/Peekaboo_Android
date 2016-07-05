package com.peekaboo.data;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.peekaboo.domain.Error;
import com.peekaboo.domain.ErrorHandler;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by sebastian on 05.07.16.
 */
public class ResponseErrorHandler implements ErrorHandler {
    private Gson gson = new Gson();
    private Context context;

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
            } catch (Exception e) {}
        }
        return t.getMessage();
    }
}
