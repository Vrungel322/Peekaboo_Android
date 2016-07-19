package com.peekaboo.data.mappers;

import com.google.gson.Gson;
import com.peekaboo.presentation.services.Message;

/**
 * Created by sebastian on 12.07.16.
 */
public class MessageJsonMapper implements Mapper<Message, String> {
    private Gson gson = new Gson();

    @Override
    public String transform(Message obj) throws RuntimeException {
        return gson.toJson(obj);
    }


}
