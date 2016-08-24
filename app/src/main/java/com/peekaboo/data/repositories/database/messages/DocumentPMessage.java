package com.peekaboo.data.repositories.database.messages;

import com.peekaboo.utils.Constants;

/**
 * Created by st1ch on 08.08.2016.
 */
public class DocumentPMessage extends PMessage {
    public DocumentPMessage(String packageId, boolean isMine, String messageBody, long timestamp,
                            int status, String receiverId, String senderId) {
        super(packageId, isMine, PMessageAbs.PMESSAGE_MEDIA_TYPE.DOCUMENT_MESSAGE, messageBody, timestamp,
                status, receiverId, senderId);
    }
}
