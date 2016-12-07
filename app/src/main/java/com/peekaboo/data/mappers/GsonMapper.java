package com.peekaboo.data.mappers;

import com.google.gson.Gson;

/**
 * Created by arkadii on 12/7/16.
 */
public class GsonMapper implements JsonMapper {
    private final Gson gson = new Gson();

    @Override
    public String toJson(Object o) {
        return gson.toJson(o);
    }

    @Override
    public <T> T fromJson(String o, Class<T> classOfT) {
        return gson.fromJson(o, classOfT);
    }
}
