package com.peekaboo.presentation.utils;

import java.util.HashMap;

/**
 * Created by sebastian on 07.09.16.
 */
public interface AudioPlayer {
    int STATE_RESET = 0;
    int STATE_PREPARED = 1;
    int STATE_PLAYING = 2;

    /**
     * called when state is {@link #STATE_RESET}
     *
     * @param url
     * @param headers
     * @param listener
     */
    void prepare(long audioId, String url, HashMap<String, String> headers, AudioPlayerListener listener);

    /**
     * can be called when state is {@link #STATE_PREPARED}
     */
    void start();

    /**
     * can be called when state is {@link #STATE_PLAYING}
     */
    void pause();

    void reset();

    int state();

    interface AudioPlayerListener {
        void onStartPlaying(long id);

        void onStopPlaying(long id);

        void onProgressChanged(long id, long position, long duration);
    }
}
