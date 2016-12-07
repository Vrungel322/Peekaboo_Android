package com.peekaboo.data;

import android.media.AudioFormat;

/**
 * Created by sebastian on 13.06.16.
 */
public interface Constants {
//    String BASE_URL = "http://10.0.1.34:8080/";
//    String DOMEN = "http://b34703ca.ngrok.io/";
    String DOMEN = "95.85.24.64"; // remote server
//    String DOMEN = "10.0.1.13"; // Boroda server
//    String DOMEN = "10.0.1.33"; // Igor server
//    String DOMEN = "10.0.1.14"; // Lesha server
    String BASE_URL = "http://" + DOMEN + ":8080/";
//    String BASE_URL = "http://b34703ca.ngrok.io/";
    String BASE_URL_SOCKET = "ws://" + DOMEN + ":8080/socket/send";
//    String BASE_URL_SOCKET = "ws://10.0.1.14:8080/socket";
//    String BASE_URL_SOCKET = "ws://b34703ca.ngrok.io/socket/send";
//    String BASE_URL = "http://192.168.1.115:8080/";

    interface CACHE{
        String BIG_CACHE_PATH = "picasso_big_cache" ;
        int    MIN_DISK_CACHE_SIZE = 32 * 1024 * 1024;       // 32MB
        int    MAX_DISK_CACHE_SIZE = 512 * 1024 * 1024;      // 512MB
        float  MAX_AVAILABLE_SPACE_USE_FRACTION = 0.9f;
        float  MAX_TOTAL_SPACE_USE_FRACTION     = 0.25f;
    }

    interface SOUND_RECORDING {
        String LOG_TAG = "SOUND_RECORDING";
        int RECORDER_BPP = 16;
        String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
        String AUDIO_RECORDER_FOLDER = "PeekabooAudio";
        String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";
        int RECORDER_SAMPLERATE = 44100;
        int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
        int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    }

    interface IMAGE_SIZES {
        int AVATAR_SIZE = 500;
        int IMAGE_SIZE = 500;
    }
}
