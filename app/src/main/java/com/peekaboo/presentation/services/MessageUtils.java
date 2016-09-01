package com.peekaboo.presentation.services;

import android.util.Log;

import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.data.repositories.database.messages.PMessageAbs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MessageUtils {
    public static Message createTextMessage(String message, String receiver, String from) {
        return new Message(Message.Command.SEND)
                .addParam(Message.Params.DESTINATION, receiver)
                .addParam(Message.Params.FROM, from)
                .addParam(Message.Params.TYPE, Message.Type.TEXT)
                .setTextBody(message);
    }

    public static Message createSwitchModeMessage(byte mode) {
        return new Message(Message.Command.SWITCHMODE)
                .setBody(new byte[]{mode});
    }

    public static List<Message> createFileMessage(Message message, String filename, int partSize) {
        List<Message> result = new ArrayList<>();
        File file = new File(filename);
        FileInputStream stream;
        int fileLength = (int) file.length();

        try {
            stream = new FileInputStream(file);
            while (stream.available() > 0) {
                byte[] bytes = new byte[Math.min(stream.available(), partSize)];
//                stream.read()
                Log.e("available", String.valueOf(stream.available()));
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

    public static Message createTypeMessage(String receiver, String type, String body, String from) {
        return new Message(Message.Command.SEND)
                .addParam(Message.Params.DESTINATION, receiver)
                .addParam(Message.Params.TYPE, type)
                .addParam(Message.Params.FROM, from)
                .setBody(body.getBytes());
    }

    public static Message convert(PMessage message) {
        Message result;

        result = new Message(Message.Command.SEND);

        String type = message.mediaType() == PMessageAbs.PMESSAGE_MEDIA_TYPE.TEXT_MESSAGE ?
                Message.Type.TEXT : Message.Type.AUDIO;
        result.addParam(Message.Params.TYPE, type);
        result.addParam(Message.Params.DESTINATION, message.receiverId());
        result.addParam(Message.Params.ID, message.packageId());
        result.setBody(message.messageBody().getBytes(Message.UTF_8));

        return result;
    }


    public static PMessage convert(String receiverId, Message message) {


        String packageId = message.getParams().get(Message.Params.ID);
        int mediaType = 0;
        String messageMediaType = message.getParams().get(Message.Params.TYPE);
        if (messageMediaType != null) {
            mediaType = messageMediaType.equals(Message.Type.TEXT) ?
                    PMessageAbs.PMESSAGE_MEDIA_TYPE.TEXT_MESSAGE
                    : PMessageAbs.PMESSAGE_MEDIA_TYPE.AUDIO_MESSAGE;
        }
        byte[] bodyBytes = message.getBody();
        String body = "";

        if (bodyBytes != null && messageMediaType != null) {
            body = messageMediaType.equals(Message.Type.TEXT) ? message.getTextBody() : new String(bodyBytes);
        }
        String senderId = message.getParams().get(Message.Params.FROM);

        return new PMessage(
                packageId,
                false,
                mediaType,
                body,
                System.currentTimeMillis(),
                PMessageAbs.PMESSAGE_STATUS.STATUS_DELIVERED,
                receiverId,
                senderId
        );
    }


    public static Message createReadMessage(String senderId, String from) {
        return new Message(Message.Command.READ)
                .addParam(Message.Params.DESTINATION, senderId)
                .addParam(Message.Params.FROM, from);
    }
}
