package com.peekaboo.data;

import android.media.AudioFormat;

/**
 * Created by sebastian on 13.06.16.
 */
public interface Constants {
//    String BASE_URL = "http://10.0.1.34:8080/";
//    String DOMEN = "http://b34703ca.ngrok.io/";
    String DOMEN = "192.168.33.105";
    String BASE_URL = "http://" + DOMEN + ":8080/";
//    String BASE_URL = "http://b34703ca.ngrok.io/";
    String BASE_URL_SOCKET = "ws://" + DOMEN + ":8080/socket/send";
//    String BASE_URL_SOCKET = "ws://b34703ca.ngrok.io/socket/send";
//    String BASE_URL = "http://192.168.1.115:8080/";


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
}
