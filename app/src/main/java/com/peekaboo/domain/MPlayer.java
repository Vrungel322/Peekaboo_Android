package com.peekaboo.domain;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * Created by st1ch on 05.08.2016.
 */
public class MPlayer {

    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;

    public Observable<MediaPlayer> getMPlayer() {
        return Observable.just(mediaPlayer)
                .subscribeOn(Schedulers.computation());
    }

    public Subscription play(String filepath) {
        if (TextUtils.isEmpty(filepath)) return null;
        return getMPlayer().subscribe(mediaPlayer1 -> {
            try {
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public Subscription stop() {
        return getMPlayer().subscribe(mediaPlayer1 -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            isPlaying = false;
        });
    }

    public Subscription stopAndPlay(String filepath){
        if(isPlaying) return stop();
        return play(filepath);
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}
