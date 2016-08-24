package com.peekaboo.data.repositories.database.messages;

import com.peekaboo.utils.Constants;

/**
 * Created by st1ch on 08.08.2016.
 */
public class TextPMessage extends PMessage {
    public TextPMessage(String packageId, boolean isMine, String messageBody, long timestamp,
                        int status, String receiverId, String senderId) {
        super(packageId, isMine, PMessageAbs.PMESSAGE_MEDIA_TYPE.TEXT_MESSAGE, messageBody, timestamp,
                status, receiverId, senderId);
    }
}
