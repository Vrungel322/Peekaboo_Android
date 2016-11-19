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
import com.peekaboo.utils.MainThread;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WebSocketNotifier implements INotifier<Message> {
    private static final String AUTHORIZATION = "Authorization";
    private static final String TAG = "socket";
    public static final int PING_INTERVAL = 20 * 1000;

    private final String BASE_URL;
    private final int TIMEOUT;
    private final Mapper<Message, byte[]> mtb;
    private final Mapper<byte[], Message> btm;
    private final Set<NotificationListener<Message>> listeners = new HashSet<>();
    private MainThread mainThread;
    @Nullable
    private WebSocket ws;
    @Nullable
    private String authorization;

    public WebSocketNotifier(String baseUrl, int timeout, AbstractMapperFactory abstractMapperFactory, MainThread mainThread) {
        this.BASE_URL = baseUrl;
        this.TIMEOUT = timeout;
        this.mainThread = mainThread;
        mtb = abstractMapperFactory.getMessageToByteMapper();
        btm = abstractMapperFactory.getByteToMessageMapper();
    }

    private void connectSocket(String authorization) {
        if (ws == null) {
            try {
                ws = new WebSocketFactory()
                        .createSocket(BASE_URL, TIMEOUT)
                        .setPingInterval(PING_INTERVAL)
                        .addListener(new WebSocketAdapter() {
                            @Override
                            public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                                Log.e(TAG, "Status: Connected to " + BASE_URL);
                                mainThread.run(() -> {
                                    for (NotificationListener<Message> listener : listeners) {
                                        listener.onConnected();
                                    }
                                });
                            }

                            @Override
                            public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
                                Log.e(TAG, "Status: Error " + cause);
                                mainThread.run(() -> {
                                    disconnect();
                                });
                            }

                            @Override
                            public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame,
                                                       WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                                Log.e(TAG, "Status: Disconnected ");

                                mainThread.run(() -> {
                                    abandonSocket();

                                    for (NotificationListener<Message> listener : listeners) {
                                        listener.onDisconnected();
                                    }
                                    if (WebSocketNotifier.this.authorization != null) {
                                        connectSocket(WebSocketNotifier.this.authorization);
                                    }
                                });
                            }

                            @Override
                            public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {
                                Log.e(TAG, "Status: Binary Message received");

                                mainThread.run(() -> {

                                    Message obtainedMessage = btm.transform(binary);
                                    Log.e(TAG, "Status: transformed to " + obtainedMessage + " " + listeners);

                                    for (NotificationListener<Message> listener : listeners) {
                                        listener.onMessageObtained(obtainedMessage);
                                    }

                                });

                            }

                            @Override
                            public void onPongFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
                                Log.e(TAG, "Status: Pong received " + frame);
//                                mainThread.run(() -> {
//                                    for (NotificationListener<Message> listener : listeners) {
//                                        listener.onConnected();
//                                    }
//                                });
                            }
                        })
                        .addHeader(AUTHORIZATION, authorization)
                        .connectAsynchronously();
            } catch (IOException e) {
                disconnect();
                Log.e(TAG, "exception " + e);
            }
        }
    }

    @Override
    public void tryConnect(String authorization) {
        Log.e(TAG, "try connect " + ws);
        this.authorization = authorization;
        connectSocket(authorization);
    }

    @Override
    public boolean isAvailable() {
        return ws != null && ws.isOpen();
    }

    @Override
    public void disconnect() {
        this.authorization = null;
        abandonSocket();
    }

    private void abandonSocket() {
        if (ws != null) {
            ws.disconnect();
            ws = null;
        }
    }

    @Override
    public void sendMessage(Message message) {
        Log.e(TAG, "send message " + message);
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
