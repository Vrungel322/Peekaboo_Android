package com.peekaboo.domain;

import com.peekaboo.presentation.services.Message;

public class MessageUtils {
    public static Message createTextMessage(String message, String receiver) {
        return new Message(Message.Command.SEND)
                .setTextBody(message)
                .addParam(Message.Params.DESTINATION, receiver)
                .addParam(Message.Params.TYPE, Message.Type.TEXT);
        //        result.addParam(Message.Params.DATE, );
    }
}
