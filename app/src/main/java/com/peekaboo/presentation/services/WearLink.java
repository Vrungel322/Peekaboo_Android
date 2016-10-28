package com.peekaboo.presentation.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.data.repositories.database.messages.PMessageAbs;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.domain.Record;
import com.peekaboo.domain.SessionRepository;
import com.peekaboo.presentation.PeekabooApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by mkizub on 24.09.16.
 */

public class WearLink extends Service implements IMessenger.MessengerListener
{
    public static final String TAG = "WearLink";

    @Inject
    IMessenger notifier;
    @Inject
    AccountUser accountUser;
    @Inject
    SessionRepository sessionRepository;

    private final ChatRequestImpl chatRequest = new ChatRequestImpl();

    private static final int MSG_POST_CHAT_MESSAGE = 1;

    private Handler handler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MSG_POST_CHAT_MESSAGE:
                notifier.sendMessage((PMessage) msg.obj);
                return;
            }
        }
    };

    private static final String predefinedContacts =
            "[" +
            "   {" +
            "      \"id\":\"386\"," +
            "      \"name\":\"pomaranch\"" +
            "   }," +
            "   {" +
            "      \"id\":\"480\"," +
            "      \"name\":\"Nikitos\"" +
            "   }," +
            "   {" +
            "      \"id\":\"537\"," +
            "      \"name\":\"ac2epsilon\"" +
            "   }," +
            "   {" +
            "      \"id\":\"448\"," +
            "      \"name\":\"ikasyk\"" +
            "   }," +
            "   {" +
            "      \"id\":\"476\"," +
            "      \"name\":\"Arkadius\"" +
            "   }," +
            "   {" +
            "      \"id\":\"578\"," +
            "      \"name\":\"patrikstarman\"" +
            "   }," +
            "   {" +
            "      \"id\":\"121\"," +
            "      \"name\":\"AidenEagleton\"" +
            "   }," +
            "   {" +
            "      \"id\":\"202\"," +
            "      \"name\":\"JohnEagleton\"" +
            "   }," +
            "   {" +
            "      \"id\":\"629\"," +
            "      \"name\":\"userone\"" +
            "   }," +
            "   {" +
            "      \"id\":\"138\"," +
            "      \"name\":\"Vrungel\"" +
            "   }," +
            "   {" +
            "      \"id\":\"140\"," +
            "      \"name\":\"Oleksii\"" +
            "   }," +
            "   {" +
            "      \"id\":\"160\"," +
            "      \"name\":\"Vozhak\"" +
            "   }," +
            "   {" +
            "      \"id\":\"166\"," +
            "      \"name\":\"st1ch\"" +
            "   }" +
            "]";

    class ChatRequestImpl extends ChatRequest.Stub {
        ChatListener listener;

        @Override
        public String post(String action, Map params, String data) throws RemoteException {
            if ("attach".equals(action)) {
                try {
                    JSONObject json = new JSONObject();
                    json.put("id", accountUser.getId());
                    json.put("name", accountUser.getUsername());
                    json.put("contacts", new JSONArray(predefinedContacts));
//                    final JSONArray jarr = new JSONArray();
//                    sessionRepository.getAllSavedContacts().forEach(contacts -> {
//                        for (Contact c : contacts) {
//                            try {
//                                JSONObject jc = new JSONObject();
//                                jc.put("id", c.contactId());
//                                jc.put("name", c.contactNickname());
//                                jarr.put(jc);
//                            } catch (JSONException e) {}
//                        }
//                        Log.e(TAG, "contacts: " + jarr);
//                    });
                    return json.toString();
                } catch (Exception e) {
                    Log.e(TAG, "Error on client attach", e);
                }
                return null;
            }
            if ("list-messages".equals(action)) {
                try {
                    String id = (String) params.get("id");
                    notifier.getAllMessages(id).forEach(messages -> {
                        for (PMessage msg : messages)
                            onMessageUpdated(msg);
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Error listing messages", e);
                }
                return null;
            }
            if ("post".equals(action)) {
                try {
                    PMessage message = null;
                    if (params.containsKey("audio")) {
                        int rate = Integer.parseInt((String) params.get("rate"));
                        if (rate < 8000 || rate > 44100) {
                            Log.e(TAG, "Expecting sampkle rate between 8000 and 44100, got " + rate);
                            return null;
                        }
                        if (!"PCM16".equals(params.get("fmt"))) {
                            Log.e(TAG, "Expecting PCM16 fmt, got " + params.get("fmt"));
                            return null;
                        }
                        JSONObject json = new JSONObject(data);
                        long timestamp = json.optLong("timestamp", System.currentTimeMillis());
                        String receiverId = json.optString("receiver", accountUser.getId());
                        String senderId = json.optString("sender", accountUser.getId());
                        File file = new File((String) params.get("audio"));
                        if (!file.exists() || !file.canRead() || !file.isFile() || file.length() == 0) {
                            Log.e(TAG, "File not exists or not readable: " + file);
                            return null;
                        }
                        Record record = new Record(accountUser.getId());
                        long totalAudioLen = file.length();
                        long totalDataLen = totalAudioLen + 36;
                        long longSampleRate = rate;
                        int channels = 1;
                        long byteRate = 16 * longSampleRate * channels / 8;
                        Log.e("WearLink", file + " " + record.getFilename());
                        FileInputStream inp = new FileInputStream(file);
                        FileOutputStream out = new FileOutputStream(record.getFilename());
                        record.WriteWaveFileHeader(out, totalAudioLen, totalDataLen, longSampleRate, 1, byteRate);
                        byte[] buff = new byte[16 * 1024];
                        int sz;
                        while ((sz = inp.read(buff)) > 0)
                            out.write(buff, 0, sz);
                        out.close();
                        inp.close();
//                        file.delete();
                        new FileOutputStream(file, false).close();
                        Log.e("WearLink", "file length " + new File((String) params.get("audio")).length());
                        Log.e("WearLink", "" + new File((String) params.get("audio")).exists());
                        message = new PMessage(true, PMessage.PMESSAGE_MEDIA_TYPE.AUDIO_MESSAGE,
                                record.getFilename(), timestamp,
                                PMessage.PMESSAGE_STATUS.STATUS_SENT,
                                receiverId, senderId);
                    } else {
                        JSONObject json = new JSONObject(data);
                        long timestamp = json.optLong("timestamp", System.currentTimeMillis());
                        String receiverId = json.optString("receiver", accountUser.getId());
                        String senderId = json.optString("sender", accountUser.getId());
                        String text = json.getString("text");
                        message = new PMessage(true, PMessage.PMESSAGE_MEDIA_TYPE.TEXT_MESSAGE,
                                text, timestamp,
                                PMessage.PMESSAGE_STATUS.STATUS_SENT,
                                receiverId, senderId);
                    }
                    if (message != null)
                        handler.obtainMessage(MSG_POST_CHAT_MESSAGE, message).sendToTarget();
                } catch (Exception e) {
                    Log.e(TAG, "Error saving wav file", e);
                }
                return null;
            }
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
        return chatRequest;
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
        if (message.mediaType() != PMessage.PMESSAGE_MEDIA_TYPE.TEXT_MESSAGE)
            return;
        String text = message.messageBody();
        if (text == null || text.length() == 0)
            return;
        try {
            ChatListener listener = chatRequest.listener;
            if (listener != null)
                listener.onMessage("updated", encodeMessage(message));
        } catch (DeadObjectException e) {
            Log.e(TAG, "Chat listener died", e);
            chatRequest.listener = null;
        } catch (RemoteException e) {
            Log.e(TAG, "Cannot send message to chat listener", e);
        }
    }

    @Override
    public int displayStatus(PMessage message) {
        return IMessenger.MessengerListener.STATUS_IGNORE;
    }
//
//    @Override
//    public int willChangeStatus(PMessage message) {
//        return message.status();
//    }

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
            if (message.mediaType() == PMessageAbs.PMESSAGE_MEDIA_TYPE.TEXT_MESSAGE)
                json.put("text", message.messageBody());
            else if (message.mediaType() == PMessageAbs.PMESSAGE_MEDIA_TYPE.AUDIO_MESSAGE)
                json.put("audio", message.messageBody());
            return json.toString();
        } catch (JSONException e) {
            Log.e(TAG, "mesage encode error", e);
            return null;
        }
    }

}
