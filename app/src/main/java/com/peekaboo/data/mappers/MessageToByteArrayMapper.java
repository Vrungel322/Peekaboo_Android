package com.peekaboo.data.mappers;

import com.peekaboo.presentation.services.Message;

import java.util.Map;

public class MessageToByteArrayMapper implements Mapper<Message, byte[]> {

    @Override
    public byte[] transform(Message obj) throws RuntimeException {
        byte[] result;
        byte[] body = obj.getBody();

        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%s\n", obj.getCommand().name()));
        for (Map.Entry<Message.Params, String> param : obj.getParams().entrySet()) {
            builder.append(String.format("%s:%s\n", param.getKey().name().toLowerCase(), param.getValue()));
        }

        if (body != null) {
            builder.append("\n");
        }

        byte[] head = builder.toString().getBytes(Message.UTF_8);
        if (body == null) {
            result = head;
        } else {
            int headLength = head.length;
            int bodyLength = body.length;
            result = new byte[headLength + bodyLength];
            System.arraycopy(head, 0, result, 0, headLength);
            System.arraycopy(body, 0, result, headLength, bodyLength);
        }
        return result;
    }
}
