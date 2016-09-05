package com.peekaboo.presentation.services;

import android.support.annotation.Nullable;
import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.peekaboo.data.mappers.AbstractMapperFactory;
import com.peekaboo.data.mappers.Mapper;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WebSocketNotifier implements INotifier<Message> {
    private static final String AUTHORIZATION = "Authorization";
    private static final String TAG = "socket";

    private final String BASE_URL;
    private final int TIMEOUT;
    private final Mapper<Message, byte[]> mtb;
    private final Mapper<byte[], Message> btm;
    private final Set<NotificationListener<Message>> listeners = new HashSet<>();

    @Nullable
    private WebSocket ws;

    public WebSocketNotifier(String baseUrl, int timeout, AbstractMapperFactory abstractMapperFactory) {
        this.BASE_URL = baseUrl;
        this.TIMEOUT = timeout;
        mtb = abstractMapperFactory.getMessageToByteMapper();
        btm = abstractMapperFactory.getByteToMessageMapper();
    }

    private void connectSocket(String authorization) {
        if (ws == null) {
                try {
                    ws = new WebSocketFactory()
                            .createSocket(BASE_URL, TIMEOUT)
                            .addListener(new WebSocketAdapter() {
                                @Override
                                public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                                    String msg = "Status: Connected to " + BASE_URL;
                                    Log.e(TAG, msg);
                                    for (NotificationListener<Message> listener : listeners) {
                                        listener.onConnected();
                                    }
                                }

                                @Override
                                public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
                                    String msg = "Status: Error " + cause;
                                    Log.e(TAG, msg);
                                    ws = null;
                                }

                                @Override
                                public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame,
                                                           WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                                    String msg = "Status: Disconnected ";
                                    Log.e(TAG, msg);
                                    ws = null;

                                    for (NotificationListener<Message> listener : listeners) {
                                        listener.onDisconnected();
                                    }
                                }

                                @Override
                                public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {
                                    String msg = "Status: Binary Message received";
                                    Log.e(TAG, msg);

                                    Message obtainedMessage = btm.transform(binary);
                                    Log.e(TAG, "Status: Text Message received" + obtainedMessage);

                                    for (NotificationListener<Message> listener : listeners) {
                                        listener.onMessageObtained(obtainedMessage);
                                    }

                                }

                                @Override
                                public void onPongFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
                                    String msg = "Status: Pong received " + frame;
                                    Log.e(TAG, msg);
                                }
                            })
                            .addHeader(AUTHORIZATION, authorization)
                            .connectAsynchronously();
                } catch (IOException e) {
                    ws = null;
                    Log.e(TAG, "exception " + e);
                }
            }
    }


    @Override
    public void tryConnect(String authorization) {
        Log.e("socket", "try connect " + ws);
        connectSocket(authorization);
    }

    @Override
    public boolean isAvailable() {
        return ws != null && ws.isOpen();
    }

    @Override
    public void disconnect() {
        if (ws != null) {
            ws.disconnect();
            ws = null;
        }
    }

    @Override
    public void sendMessage(Message message) {
        Log.e("notifier", "send message " + message);
        sendBinaryMessage(mtb.transform(message));
    }

    public void sendBinaryMessage(byte[] message) {
        if (ws != null) {
            ws.sendBinary(message);
        }
    }

    @Override
    public void addListener(NotificationListener<Message> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(NotificationListener<Message> listener) {
        listeners.remove(listener);
    }
}
