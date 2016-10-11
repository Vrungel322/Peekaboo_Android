package com.peekaboo.presentation.utils;

/**
 * Created by arkadius on 10/4/16.
 */


public class AudioIdManager {
    private static String DIVIDER = " ";

    public static long getMessageId(String audioId) {
        if (audioId != null) {
            return Long.parseLong(audioId.split(DIVIDER)[1]);
        } else {
            return 0;
        }
    }

    public static String getCompanionId(String audioId) {
        if (audioId != null) {
            return audioId.split(DIVIDER)[0];
        } else {
            return "";
        }

    }

    public static String constructId(String companionId, long messageId) {
        return companionId + DIVIDER + messageId;
    }
}
