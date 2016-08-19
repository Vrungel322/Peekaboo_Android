package com.peekaboo.domain;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by st1ch on 05.08.2016.
 */
public class MPlayer {

    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;

    public void play(String filepath) throws IOException {
        if (!TextUtils.isEmpty(filepath)) {
            FileInputStream fis = new FileInputStream(filepath);
            FileDescriptor fd = fis.getFD();
            if (fd != null) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(fd);
                mediaPlayer.prepare();
                mediaPlayer.setOnPreparedListener(mp1 -> {
                    mediaPlayer.start();
                    isPlaying = true;
                });
                mediaPlayer.setOnCompletionListener(mp -> {
                    mediaPlayer.release();
                    mediaPlayer = null;
                    isPlaying = false;
                });
            }
        }
    }

    public void stop() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        isPlaying = false;
    }

    public void stopAndPlay(String filepath) throws IOException {
        if (isPlaying){
            stop();
        } else {
            play(filepath);
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}
