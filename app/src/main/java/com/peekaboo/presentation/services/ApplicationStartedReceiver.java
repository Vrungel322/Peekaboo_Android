package com.peekaboo.presentation.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by sebastian on 11.07.16.
 */
public class ApplicationStartedReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Log.e("socket", "app start onReceive()");
        NotificationService.launch(context, NotificationService.ACTION.TRY_CONNECT);
    }
}
