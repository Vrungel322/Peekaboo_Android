package com.peekaboo.presentation.utils;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.peekaboo.utils.MainThread;

/**
 * Created by sebastian on 07.09.16.
 */
public class AsyncExecutor extends HandlerThread {

    private Handler workerHandler;

    public AsyncExecutor(String name) {
        super(name);
    }

    public void post(int actionId, Runnable runnable) {
        workerHandler.removeMessages(actionId);
        Message msg = workerHandler.obtainMessage(actionId, runnable);
        workerHandler.sendMessage(msg);
    }

    public void prepareHandler() {
        workerHandler = new Handler(getLooper(), msg -> {
            Runnable task = (Runnable) msg.obj;
            task.run();
            return true;
        });
    }
}
