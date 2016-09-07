package com.peekaboo.domain;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;

import java.io.IOException;

/**
 * Created by st1ch on 05.08.2016.
 */
public class MPlayer {

    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;

    public MPlayer() {
        mediaPlayer = new MediaPlayer();
    }

    public void play(String filepath) throws IOException {
        if (!TextUtils.isEmpty(filepath)) {
//            mediaPlayer = new MediaPlayer();
            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(filepath);
            mediaPlayer.setScreenOnWhilePlaying(true);
            mediaPlayer.prepare();

            mediaPlayer.setOnPreparedListener(mp1 -> {
                mediaPlayer.start();
                isPlaying = true;
            });
            mediaPlayer.setOnCompletionListener(mp -> {
                stop();
                isPlaying = false;
            });

        }
    }

    public void stop() throws IllegalStateException {
        mediaPlayer.stop();
        mediaPlayer.release();
        isPlaying = false;
    }

    public void stopOrPlay(String filepath) throws IOException {
        if (isPlaying) {
            stop();
        } else {
            play(filepath);
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}
