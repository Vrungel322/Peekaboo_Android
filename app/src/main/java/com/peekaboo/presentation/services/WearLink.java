package com.peekaboo.presentation.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.presentation.PeekabooApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.inject.Inject;

/**
 * Created by mkizub on 24.09.16.
 */

public class WearLink extends Service implements IMessenger.MessengerListener
{
    public static final String TAG = "WearLink";

    @Inject
    IMessenger notifier;

    private final List<ChatRequestImpl> chatRequests = new CopyOnWriteArrayList<ChatRequestImpl>();

    class ChatRequestImpl extends ChatRequest.Stub {
        ChatListener listener;

        @Override
        public String post(String action, Map params, String data) throws RemoteException {
            return null;
        }

        @Override
        public void listen(ChatListener listener) throws RemoteException {
            this.listener = listener;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind intent =" + intent);
        ChatRequestImpl impl = new ChatRequestImpl();
        chatRequests.add(impl);
        return impl;
    }

    public static void launch(Context context) {
        context.startService(new Intent(context, WearLink.class));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PeekabooApplication.getApp(this).getComponent().inject(this);
        notifier.addMessageListener(this);
        Intent intent = new Intent("com.peekaboo.started");
        sendBroadcast(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    // IMessageListener

    @Override
    public void onMessageUpdated(PMessage message) {
        if (!message.isMine())
            return;
        if (message.mediaType() != PMessage.PMESSAGE_MEDIA_TYPE.TEXT_MESSAGE)
            return;
        String text = message.messageBody();
        if (text == null || text.length() == 0)
            return;
        for (ChatRequestImpl chat : chatRequests) {
            try {
                ChatListener listener = chat.listener;
                if (listener != null)
                    listener.onMessage("updated", encodeMessage(message));
            } catch (DeadObjectException e) {
                Log.e(TAG, "Chat listener died", e);
                chatRequests.remove(chat);
            } catch (RemoteException e) {
                Log.e(TAG, "Cannot send message to chat listener", e);
            }
        }
    }

    @Override
    public int willChangeStatus(PMessage message) {
        return message.status();
    }

    private static String encodeMessage(PMessage message) {
        try {
            JSONObject json = new JSONObject();
            json.put("id", message.id());
            json.put("own", message.isMine());
            json.put("timestamp", message.timestamp());
            json.put("receiver", message.receiverId());
            json.put("sender", message.senderId());
            switch (message.status()) {
            case PMessage.PMESSAGE_STATUS.STATUS_SENT:
                json.put("status", "sent");
                break;
            case PMessage.PMESSAGE_STATUS.STATUS_DELIVERED:
                json.put("status", "delivered");
                break;
            case PMessage.PMESSAGE_STATUS.STATUS_READ:
                json.put("status", "read");
                break;
            }
            json.put("text", message.messageBody());
            return json.toString();
        } catch (JSONException e) {
            Log.e(TAG, "mesage encode error", e);
            return null;
        }
    }

}
