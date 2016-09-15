package com.peekaboo.presentation.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.peekaboo.utils.MainThread;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sebastian on 07.09.16.
 */
@Singleton
public class AsyncAudioPlayer implements AudioPlayer, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    public static final int ACTION_ID = 1010;
    public static final int UPDATE_PROGRESS_INTERVAL_MILLIS = 500;
    private final MainThread mainThread;
    private Context context;
    private AsyncExecutor executor;
    private MediaPlayer player;
    @Nullable
    private AudioPlayerListener listener;
    private int state;
    private long audioId;
    private Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            if (state() == STATE_PLAYING) {
                if (listener != null) {
                    Log.e("player", "progress " + player.getCurrentPosition() + " " + player.getDuration());
                    listener.onProgressChanged(getAudioId(), player.getCurrentPosition(), player.getDuration());
                }
                mainThread.runDelayed(progressRunnable, UPDATE_PROGRESS_INTERVAL_MILLIS);
            }
        }

    };
    private StringRunnable prepareRunnable = new StringRunnable() {
        @Override
        public void run() {
            Log.e("player", "prepare " + state());
            try {
                player.setDataSource(getUrl());
                player.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    @Inject
    public AsyncAudioPlayer(Context context, MainThread mainThread) {
        this.context = context;
        this.mainThread = mainThread;
        initMediaPlayer(context);
    }

    public long getAudioId() {
        return audioId;
    }

    public void setListener(@Nullable AudioPlayerListener listener) {
        this.listener = listener;
        notifyListener(listener);
        if (state() == STATE_PLAYING && listener != null) {
            mainThread.remove(progressRunnable);
            mainThread.run(progressRunnable);
        } else if (listener == null) {
            mainThread.remove(progressRunnable);
        }
    }

    private void notifyListener(@Nullable final AudioPlayerListener listener) {
        final long audioId = getAudioId();
        final int state = state();
        mainThread.run(() -> {
            Log.e("notify", audioId + " " + state);
            if (listener != null)
                switch (state) {
                    case STATE_PLAYING:
                        Log.e("player", "notify start " + audioId);
                        listener.onStartPlaying(audioId);
                        break;
                    case STATE_RESET:
                    case STATE_PREPARED:
                        listener.onStopPlaying(audioId);
                        break;
                }
        });
    }

    private void initMediaPlayer(Context context) {
        player = new MediaPlayer();
        player.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnErrorListener(this);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.reset();
    }

    public void preparePlayer() {
        executor = new AsyncExecutor("player");
        executor.start();
        executor.prepareHandler();
    }

    public boolean isInitialized() {
        return executor != null;
    }

    @Override
    public void prepare(long audioId, String uri, AudioPlayerListener listener) {
        Log.e("player", "prepare " + uri);
        this.listener = listener;
        this.audioId = audioId;
        prepareRunnable.setUrl(uri);
//        prepareRunnable.setHeaders(headers);
        executor.post(ACTION_ID, prepareRunnable);
    }

    @Override
    public void start() {
        Log.e("player", "start " + state() + " " + listener);
        setState(STATE_PLAYING);
        mainThread.run(progressRunnable);
        player.start();
    }

    @Override
    public void pause() {
        Log.e("player", "pause " + state());
        setState(STATE_PREPARED);
        mainThread.remove(progressRunnable);
        player.pause();
    }

    @Override
    public void reset() {
        Log.e("player", "reset " + state());
        if (state() == STATE_PLAYING) {
            pause();
        }
        setState(STATE_RESET);
        player.reset();
    }

    public void setState(int state) {
        this.state = state;
        notifyListener(listener);
    }

    @Override
    public int state() {
        return state;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.e("player", "onPrepared " + state());
        setState(STATE_PREPARED);
        start();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e("player", "onError " + state());
        reset();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.e("player", "onCompletion " + state());
        setState(STATE_PREPARED);
    }


    private static abstract class StringRunnable implements Runnable {
        private String url;
//        private HashMap<String, String> headers;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

//        public Map<String, String> getHeaders() {
//            return headers;
//        }

//        public void setHeaders(HashMap<String, String> headers) {
//            this.headers = headers;
//        }
    }
}
