package com.peekaboo.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.peekaboo.presentation.services.NotificationService;

/**
 * Created by Nikita on 28.06.2016.
 */
public class InternetBroadcastReceiver extends BroadcastReceiver {
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        int type = getConnectivityStatus(context);

        Log.e("socket", "internet broadcast " + String.valueOf(type) + " " + hashCode() + " " + intent);
        switch (type) {
            case 0:
//                Toast.makeText(context, "No Internet Access", Toast.LENGTH_LONG).show();
                break;
            case 1:
//                Toast.makeText(context, "Internet Access OK (WIFI)", Toast.LENGTH_LONG).show();
                NotificationService.launch(context, NotificationService.ACTION.TRY_CONNECT);
                break;
            case 2:
//                Toast.makeText(context, "Internet Access OK (MOBILE)", Toast.LENGTH_LONG).show();
                NotificationService.launch(context, NotificationService.ACTION.TRY_CONNECT);
                break;
        }
    }

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            Log.e("socket", "networkState " + activeNetwork.toString());
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }
}