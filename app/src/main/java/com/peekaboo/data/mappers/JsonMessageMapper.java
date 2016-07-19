package com.peekaboo.data.mappers;

import com.google.gson.Gson;
import com.peekaboo.presentation.services.Message;

/**
 * Created by sebastian on 12.07.16.
 */
public class JsonMessageMapper implements Mapper<String, Message> {

    private Gson gson = new Gson();

    @Override
    public Message transform(String obj) throws RuntimeException {
        return gson.fromJson(obj, Message.class);
    }
}
