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
        remove(runnable);
        handler.post(runnable);
    }

    public void runDelayed(Runnable runnable, int millis) {
        remove(runnable);
        handler.postDelayed(runnable, millis);
    }

    public void remove(Runnable runnable) {
        handler.removeCallbacks(runnable);
    }
}
