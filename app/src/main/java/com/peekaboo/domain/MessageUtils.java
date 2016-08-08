package com.peekaboo.domain;

import android.util.Log;

import com.peekaboo.presentation.services.Message;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;

public class MessageUtils {
    public static Message createTextMessage(String message, String receiver) {
        return new Message(Message.Command.SEND)
                .setTextBody(message)
                .addParam(Message.Params.DESTINATION, receiver)
                .addParam(Message.Params.TYPE, Message.Type.TEXT);
    }

    public static List<Message> createFileMessage(Message message, String filename, int partSize) {
        List<Message> result = new ArrayList<>();
        File file = new File(filename);
        FileInputStream stream = null;
        int fileLength = (int) file.length();
        int filesCount = fileLength / partSize + 1;
        int shift = 0;

        try {
            stream = new FileInputStream(file);
            for (int i = 0; i < filesCount; i++) {
                int bytesLeft = fileLength - shift;
                byte[] bytes = new byte[Math.min(bytesLeft, partSize)];
                stream.read(bytes);
                result.add(copyMessage(message).setBody(bytes));
            }
            result.add(copyMessage(message).addParam(Message.Params.REASON, Message.Reason.END));
            stream.close();
        } catch (IOException e) {
            Log.e("exception", String.valueOf(e));
            result.clear();
        }
        return result;
    }


    private static Message copyMessage(Message message) {
        return new Message(message.getCommand())
                .setParams(message.getParams())
                .setBody(message.getBody());
    }

    public static Message createTypeMessage(String receiver, String type) {
        return new Message(Message.Command.SEND)
                .addParam(Message.Params.DESTINATION, receiver)
                .addParam(Message.Params.TYPE, type);
    }
}
