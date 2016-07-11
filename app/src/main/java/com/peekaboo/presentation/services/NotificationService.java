package com.peekaboo.presentation.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.BuildConfig;
import android.util.Log;
import android.widget.Toast;

public class NotificationService extends Service {
    private final IBinder binder = new NotificationBinder();

    public static void launch(Context context, @Nullable String action) {
        Intent intent = new Intent(context, NotificationService.class);
        if (action != null) {
            intent.setAction(action);
        }
        context.startService(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Intent intent = new Intent("com.peekaboo.start");
        sendBroadcast(intent);
    }

    public class NotificationBinder extends Binder {
        public NotificationService getService() {
            return NotificationService.this;
        }
    }
}
