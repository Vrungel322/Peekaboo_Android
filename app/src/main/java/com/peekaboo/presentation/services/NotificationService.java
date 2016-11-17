package com.peekaboo.presentation.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.peekaboo.domain.AccountUser;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.domain.usecase.UserModeChangerUseCase;
import com.peekaboo.utils.InternetBroadcastReceiver;

import javax.inject.Inject;

public class NotificationService extends Service {

    @Inject
    IMessenger notifier;


//    @Inject
//    MessageNotificator messageNotificator;

    @Inject
    AccountUser user;

    @Inject
    UserModeChangerUseCase userModeChangerUseCase;

    private InternetBroadcastReceiver ibrInternetCheck;

    public static void launch(Context context, @Nullable String action) {
        Intent intent = new Intent(context, NotificationService.class);
        if (action != null) {
            intent.setAction(action);
        }
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PeekabooApplication.getApp(this).getComponent().inject(this);
        Log.e("NotificationService", "onCreate() " + user.getUsername());
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
            switch (intent.getAction()) {

                case ACTION.TRY_CONNECT:
                    boolean shouldTryConnect = user.isAuthorized() && !notifier.isAvailable();
                    Log.e("socket", "onStartCommand() try connect " + shouldTryConnect + intent.hashCode());
                    if (shouldTryConnect) {
                        notifier.tryConnect(user.getBearer());
                    }
                    break;

                case "com.peekaboo.userMode.normal":

                    break;

                case "com.peekaboo.userMode.silent":
                    userModeChangerUseCase.setMode(UserModeChangerUseCase.IUserMode.TEXT_MODE);
                    break;

            }
//            if (intent.getAction().equals(ACTION.TRY_CONNECT)) {
//            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e("NotificationService", "onDestroy() " + user.getUsername());
        unregisterReceiver(ibrInternetCheck);
        Intent intent = new Intent("com.peekaboo.start");
        sendBroadcast(intent);
        super.onDestroy();
    }

    public static void stop(Context context) {
        Intent intent = new Intent(context, NotificationService.class);
        context.stopService(intent);
    }

    public interface ACTION {
        String TRY_CONNECT = "com.peekaboo.try_connect";
    }
}
