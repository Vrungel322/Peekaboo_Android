package com.peekaboo.data.repositories.database;

import com.peekaboo.utils.Constants;

/**
 * Created by st1ch on 08.08.2016.
 */
public class DocumentPMessage extends PMessage {
    public DocumentPMessage(String packageId, boolean isMine, String messageBody, long timestamp,
                            boolean isSent, boolean isDelivered, boolean isRead) {
        super(packageId, isMine, Constants.PMESSAGE_MEDIA_TYPE.DOCUMENT_MESSAGE, messageBody, timestamp,
                isSent, isDelivered, isRead);
    }
}
