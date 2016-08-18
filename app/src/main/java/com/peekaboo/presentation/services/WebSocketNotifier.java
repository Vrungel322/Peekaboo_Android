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

public class WebSocketNotifier implements INotifier {
    private static final String AUTHORIZATION = "Authorization";
    private static final String TAG = "socket";

    private final String BASE_URL;
    private final int TIMEOUT;
    private final Mapper<Message, byte[]> mtb;
    private final Mapper<byte[], Message> btm;
    private final Set<NotificationListener> listeners = new HashSet<>();

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
                                    Log.e(TAG, "Status: Connected to " + BASE_URL);
                                }

                                @Override
                                public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
                                    Log.e(TAG, "Status: Error " + cause);
                                    ws = null;
                                }

                                @Override
                                public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame,
                                                           WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                                    Log.e(TAG, "Status: Disconnected ");
                                    ws = null;
                                }

                                @Override
                                public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {
                                    Log.e(TAG, "Status: Binary Message received");
                                    Message obtainedMessage = btm.transform(binary);
                                    for (NotificationListener listener : listeners) {
                                        listener.onMessageObtained(obtainedMessage);
                                    }
                                    Log.e(TAG, "Status: Text Message received" + obtainedMessage);

                                }

                                @Override
                                public void onPongFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
                                    Log.e(TAG, "Status: Pong received " + frame);
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
        sendBinaryMessage(mtb.transform(message));
    }

    public void sendBinaryMessage(byte[] message) {
        if (ws != null) {
            ws.sendBinary(message);
        }
    }

    @Override
    public void addListener(NotificationListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(NotificationListener listener) {
        listeners.remove(listener);
    }
}
