package com.peekaboo.utils;

import android.media.AudioFormat;

/**
 * Created by st1ch on 23.07.2016.
 */
public class Constants {
    public static final String EXTRA_RECEIVER_NAME = "receiver_name";
    public static final String EXTRA_CONTACT = "contact";
    public static final String ARG_CHAT_MESSAGE_ITEM_INDEX = "message_item_index";

    public interface MESSAGE_TYPE{
        String TYPE_AUDIO    = "audio";
        String TYPE_IMAGE    = "image";
        String TYPE_VIDEO    = "video";
        String TYPE_DOCUMENT = "document";
    }

    public interface FRAGMENT_TAGS {
        String CHAT_ITEM_DIALOG_FRAGMENT_TAG = "chatItemDialog";
        String ATTACHMENT_DIALOG_FRAGMENT_TAG = "attachmentDialog";
        String DIALOGS_FRAGMENT = "dialogsFragment";
        String CALLS_FRAGMENT = "callsFragment";
        String CONTACTS_FRAGMENT = "contactsFragment";
        String PROFILE_FRAGMENT = "profileFragment";
        String SETTINGS_FRAGMENT = "settingsFragment";
        String  CHAT_FRAGMENT_TAG = "CHAT_FRAGMENT";

    }

    public interface REQUEST_CODES {
        int REQUEST_CODE_CAMERA = 1818;
        int REQUEST_CODE_GALERY = 1819;
        int REQUEST_CODE_GALERY_FOR_FRAGMENT = 1820;
        int REQUEST_CODE_SPEECH_INPUT = 1822;
    }

    public interface SOUND_RECORDING {
        String LOG_TAG = "SOUND_RECORDING";
        int RECORDER_BPP = 16;
        String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
        String AUDIO_RECORDER_FOLDER = "PeekabooAudio";
        String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";
        int RECORDER_SAMPLERATE = 16000;
        int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
        int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    }

    public interface IMAGE_SENDING {
        String IMAGE_SENDING_FOLDER = "PeekabooImage";
        String IMAGE_SENDING_FILE_EXT_JPG = ".jpg";

    }

    public interface DESIGN_CONSTANTS{
        int BIG_SIDE_MARGIN = 45;
        int TOP_OR_BOTTOM_MARGIN = 6;
        int SIDE_MARGIN = 0;
        int BIG_TOP_OR_BOTTOM_MARGIN = 8;
    }
}
