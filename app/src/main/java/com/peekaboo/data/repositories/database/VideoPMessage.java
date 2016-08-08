package com.peekaboo.data.repositories.database;

import com.peekaboo.utils.Constants;

/**
 * Created by st1ch on 08.08.2016.
 */
public class VideoPMessage extends PMessage {
    public VideoPMessage(String packageId, boolean isMine, String messageBody, long timestamp,
                         boolean isSent, boolean isDelivered, boolean isRead) {
        super(packageId, isMine, Constants.PMESSAGE_MEDIA_TYPE.VIDEO_MESSAGE, messageBody, timestamp,
                isSent, isDelivered, isRead);
    }
}
