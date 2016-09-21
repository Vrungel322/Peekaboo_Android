package com.peekaboo.data.mappers;

import android.content.ContentValues;

import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.data.repositories.database.messages.PMessageAbs;

/**
 * Created by st1ch on 23.07.2016.
 */
public class PMessageMapper implements Mapper<PMessageAbs, ContentValues> {
    @Override
    public ContentValues transform(PMessageAbs obj) throws RuntimeException {
        return new PMessageAbs.Builder()
                .isMine(obj.isMine())
                .mediaType(obj.mediaType())
                .messageBody(obj.messageBody())
                .timestamp(obj.timestamp())
                .status(obj.status())
                .senderId(obj.senderId())
                .receiverId(obj.receiverId())
                .build();
    }
}
