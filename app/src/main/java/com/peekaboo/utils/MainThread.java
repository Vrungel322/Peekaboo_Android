package com.peekaboo.utils;

import android.os.Handler;

/**
 * Created by sebastian on 06.09.16.
 */
public class MainThread {
    private Handler handler;

    public MainThread(Handler handler) {
        this.handler = handler;
    }

    public void run(Runnable runnable) {
        handler.post(runnable);
    }
}
