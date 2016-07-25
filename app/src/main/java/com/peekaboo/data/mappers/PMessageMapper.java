package com.peekaboo.data.mappers;

import android.content.ContentValues;

import com.peekaboo.data.repositories.database.PMessage;
import com.peekaboo.data.repositories.database.PMessageAbs;

/**
 * Created by st1ch on 23.07.2016.
 */
public class PMessageMapper implements Mapper<PMessage, ContentValues> {
    @Override
    public ContentValues transform(PMessage obj) throws RuntimeException {
        return new PMessageAbs.Builder()
                .packageId(obj.packageId())
                .isMine(obj.isMine())
                .messageBody(obj.messageBody())
                .timestamp(obj.timestamp())
                .isSent(obj.isSent())
                .isDelivered(obj.isDelivered())
                .isRead(obj.isRead())
                .build();
    }
}
