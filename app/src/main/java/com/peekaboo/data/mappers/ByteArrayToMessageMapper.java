package com.peekaboo.data.mappers;

//import android.util.Pair;

import com.peekaboo.data.mappers.Mapper;
import com.peekaboo.domain.Pair;
import com.peekaboo.presentation.services.Message;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ByteArrayToMessageMapper implements Mapper<byte[], Message> {
    @Override
    public Message transform(byte[] obj) throws RuntimeException {
        Pair<Message.Command, Integer> commandPair = getCommand(obj);

        Message message = new Message(commandPair.first);
        Integer paramsStart = commandPair.second;
        Pair<Map<Message.Params, String>, Integer> paramsPair = getParams(obj, paramsStart);
        message.setParams(paramsPair.first);
        int bodyStart = paramsPair.second;
        if (bodyStart < obj.length) {
            byte[] body = getBody(obj, bodyStart);
            if (Message.Type.TEXT.equals(paramsPair.first.get(Message.Params.TYPE))) {
                message.setTextBody(new String(body));
            } else {
                message.setBody(body);
            }
        }

        return message;
    }

    private byte[] getBody(byte[] obj, int bodyStart) {
        return Arrays.copyOfRange(obj, bodyStart, obj.length);
    }

    private Pair<Map<Message.Params, String>, Integer> getParams(byte[] obj, int paramsStart) {
        Map<Message.Params, String> params = new HashMap<>();
        int length = obj.length;
        int i;
        int start = paramsStart;
        for (i = paramsStart; i < length; i++) {
            if (obj[i] == '\n') {
                String paramPair = new String(Arrays.copyOfRange(obj, start, i));
                String[] split = paramPair.split(":");
                params.put(Message.Params.valueOf(split[0].toUpperCase()), split[1]);
                start = i + 1;
                if (i < length - 1 && obj[i + 1] == '\n') {
                    i = i + 1;
                    break;
                }
            }
        }
        return new Pair<>(params, i + 1);
    }

    private Pair<Message.Command, Integer> getCommand(byte[] obj) {
        Message.Command result;

        int i = 0;
        while(obj[i] != '\n') {
            i++;
        }
        String stringCommand = new String(Arrays.copyOfRange(obj, 0, i));
        result = Message.Command.valueOf(stringCommand);

        return new Pair<>(result, i + 1);
    }
}
