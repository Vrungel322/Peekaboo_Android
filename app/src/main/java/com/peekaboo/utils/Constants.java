package com.peekaboo.utils;

import android.media.AudioFormat;

/**
 * Created by st1ch on 23.07.2016.
 */
public class Constants {
    public static final String EXTRA_RECEIVER_NAME = "receiver_name";
    public static final String ARG_CHAT_MESSAGE_ITEM_INDEX = "message_item_index";

    public interface FRAGMENT_TAGS {
        String CHAT_ITEM_DIALOG_FRAGMENT_TAG = "chatItemDialog";
    }

    public interface REQUEST_CODES {
        int REQUEST_CODE_CAMERA = 1818;
        int REQUEST_CODE_GALERY = 1819;
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
}
