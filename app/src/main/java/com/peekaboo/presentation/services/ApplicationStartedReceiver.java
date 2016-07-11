package com.peekaboo.presentation.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by sebastian on 11.07.16.
 */
public class ApplicationStartedReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Log.e("receiver", "onReceive()");
        NotificationService.launch(context, null);
    }
}
