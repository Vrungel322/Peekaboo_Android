package com.peekaboo.data.repositories.database.messages;

/**
 * Created by st1ch on 08.08.2016.
 */
public class AudioPMessage extends PMessage {
    public AudioPMessage(String packageId, boolean isMine, String messageBody, long timestamp,
                         int status, String receiverId, String senderId) {
        super(packageId, isMine, PMessageAbs.PMESSAGE_MEDIA_TYPE.AUDIO_MESSAGE, messageBody, timestamp,
                status, receiverId, senderId);
    }
}
