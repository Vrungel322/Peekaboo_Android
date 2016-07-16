package com.peekaboo.presentation.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.peekaboo.data.Constants;
import com.peekaboo.domain.User;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.di.ApplicationComponent;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class NotificationService extends Service {

    public static void launch(Context context, @Nullable String action) {
        Intent intent = new Intent(context, NotificationService.class);
        if (action != null) {
            intent.setAction(action);
        }
        context.startService(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
}
