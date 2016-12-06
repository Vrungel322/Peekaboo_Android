package com.peekaboo.presentation.services;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.peekaboo.data.GroupChatCreationEntity;
import com.peekaboo.data.GroupChatMemberEntity;
import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.data.repositories.database.messages.PMessageAbs;
import com.peekaboo.domain.GroupChatMember;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MessageUtils {

    private static final String SERVER = "server";
    private static Gson gson = new Gson();

    public static Message createSwitchModeMessage(byte mode, String from) {
        return new Message(Message.Command.SYSTEMMESSAGE)
                .addParam(Message.Params.REASON, Message.Reason.MODE)
                .addParam(Message.Params.DESTINATION, SERVER)
                .addParam(Message.Params.FROM, from)
                .setBody(new byte[]{mode});
    }

    public static String createAudioBody(@Nullable String remote, @Nullable String local, boolean error) {
        String result = "";
        if (remote != null) result = remote;
        if (local != null) {
            if (result.isEmpty()) {
                result = local;
            } else {
                result = result + PMessage.DIVIDER + local;
            }
        }
        if (error) result = result + PMessage.ERROR;
        return result;
    }


    public static Message convert(PMessage message) {
        Message result;

        result = new Message(Message.Command.MESSAGE);

        String type = null;
        switch (message.mediaType()) {
            case PMessageAbs.PMESSAGE_MEDIA_TYPE.TEXT_MESSAGE:
                type = Message.Type.TEXT;
                break;
            case PMessageAbs.PMESSAGE_MEDIA_TYPE.AUDIO_MESSAGE:
                type = Message.Type.AUDIO;
                break;
            case PMessageAbs.PMESSAGE_MEDIA_TYPE.IMAGE_MESSAGE:
                type = Message.Type.IMAGE;
                break;
            case PMessageAbs.PMESSAGE_MEDIA_TYPE.GEO_MESSAGE:
                type = Message.Type.GEO;
                break;
        }
        result.addParam(Message.Params.TYPE, type);
        result.addParam(Message.Params.DESTINATION, message.receiverId());
        result.addParam(Message.Params.FROM, message.senderId());

        String s = message.messageBody();
        if (message.mediaType() != PMessage.PMESSAGE_MEDIA_TYPE.TEXT_MESSAGE) {
            s = s.split(PMessage.DIVIDER)[0];
        }
        result.setBody(s.getBytes(Message.UTF_8));

        return result;
    }

    public static Message groupChatCreateMessage(String fromId, GroupChatCreationEntity groupChatCreationEntity) {
        return new Message(Message.Command.SYSTEMMESSAGE)
                .addParam(Message.Params.FROM, fromId)
                .addParam(Message.Params.DESTINATION, SERVER)
                .addParam(Message.Params.REASON, Message.Reason.CREATE_DIALOG)
                .setTextBody(gson.toJson(groupChatCreationEntity));
    }

    public static PMessage convert(Message message) {
        int mediaType = 0;
        String messageMediaType = message.getParams().get(Message.Params.TYPE);
        if (messageMediaType != null) {
            switch (messageMediaType){
                case Message.Type.TEXT:
                    mediaType = PMessageAbs.PMESSAGE_MEDIA_TYPE.TEXT_MESSAGE;
                    break;
                case Message.Type.AUDIO:
                    mediaType = PMessageAbs.PMESSAGE_MEDIA_TYPE.AUDIO_MESSAGE;
                    break;
                case Message.Type.IMAGE:
                    mediaType = PMessageAbs.PMESSAGE_MEDIA_TYPE.IMAGE_MESSAGE;
                    break;
                case Message.Type.GEO:
                    mediaType = PMessageAbs.PMESSAGE_MEDIA_TYPE.GEO_MESSAGE;
                    break;
            }
        }
        byte[] bodyBytes = message.getBody();
        String body = "";

        if (bodyBytes != null && messageMediaType != null) {
            body = messageMediaType.equals(Message.Type.TEXT) ? message.getTextBody() : new String(bodyBytes);
        }
        String senderId = message.getParams().get(Message.Params.FROM);
        String receiverId = message.getParams().get(Message.Params.DESTINATION);

        return new PMessage(
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
        return new Message(Message.Command.SYSTEMMESSAGE)
                .addParam(Message.Params.REASON, Message.Reason.READ)
                .addParam(Message.Params.DESTINATION, senderId)
                .addParam(Message.Params.FROM, from);
    }
}
