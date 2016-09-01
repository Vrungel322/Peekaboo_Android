package com.peekaboo.presentation.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.peekaboo.domain.AccountUser;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.utils.InternetBroadcastReceiver;

import javax.inject.Inject;

public class NotificationService extends Service {

    private InternetBroadcastReceiver ibrInternetCheck;
    @Inject
    INotifier notifier;
    @Inject
    AccountUser user;

    public static void launch(Context context, @Nullable String action) {
        Intent intent = new Intent(context, NotificationService.class);
        if (action != null) {
            intent.setAction(action);
        }
//        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PeekabooApplication.getApp(this).getComponent().inject(this);
        Log.e("socket", "service onCreate()");
        Toast.makeText(this, "create service", Toast.LENGTH_LONG).show();
        ibrInternetCheck = new InternetBroadcastReceiver();
        IntentFilter ifInternetCheck = new IntentFilter();
        ifInternetCheck.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(ibrInternetCheck, ifInternetCheck);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals(ACTION.TRY_CONNECT)) {
                boolean shouldTryConnect = user.isAuthorized() && !notifier.isAvailable();
                Log.e("socket", "onStartCommand() try connect " + shouldTryConnect + intent.hashCode());
                if (shouldTryConnect) {
                    notifier.tryConnect(user.getBearer());
                }
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "destroy service", Toast.LENGTH_LONG).show();
        Log.e("socket", "service onDestroy()");
        unregisterReceiver(ibrInternetCheck);
        Intent intent = new Intent("com.peekaboo.start");
        sendBroadcast(intent);
    }

    public interface ACTION {
        String TRY_CONNECT = "com.peekaboo.try_connect";
    }
}
