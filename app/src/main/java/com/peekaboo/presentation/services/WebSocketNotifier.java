package com.peekaboo.presentation.services;

import android.support.annotation.Nullable;
import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.peekaboo.data.Constants;
import com.peekaboo.data.mappers.AbstractMapperFactory;
import com.peekaboo.data.mappers.Mapper;
import com.peekaboo.domain.MessageUtils;
import com.peekaboo.domain.User;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WebSocketNotifier implements INotifier {
    public static final String BASE_URL_SOCKET = Constants.BASE_URL_SOCKET;
    public static final int TIMEOUT = 5000;
    public static final String AUTHORIZATION = "Authorization";
    private final String TAG = "socket";

    private final User user;
    private final Mapper<Message, byte[]> mtb;
    private final Mapper<byte[], Message> btm;
    @Nullable
    private WebSocket ws;
    private Set<NotificationListener> listeners = new HashSet<>();

    public WebSocketNotifier(User user, AbstractMapperFactory abstractMapperFactory) {
        this.user = user;
        mtb = abstractMapperFactory.getMessageToByteMapper();
        btm = abstractMapperFactory.getByteToMessageMapper();
    }

    private void connectSocket() {
        if (user.isAuthorized()) {
            if (ws == null) {
                try {
                    ws = new WebSocketFactory()
                            .createSocket(BASE_URL_SOCKET, TIMEOUT)
                            .addListener(new WebSocketAdapter() {
                                @Override
                                public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                                    Log.e(TAG, "Status: Connected to " + BASE_URL_SOCKET);
                                }

                                @Override
                                public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
                                    Log.e(TAG, "Status: Error " + cause);
                                }

                                @Override
                                public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame,
                                                           WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                                    Log.e(TAG, "Status: Disconnected ");
                                    ws = null;
//                                    reconnect();
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
                            .addHeader(AUTHORIZATION, user.getBearer())
                            .connectAsynchronously();
                } catch (IOException e) {
                    Log.e(TAG, "exception " + e);
                }
            }
//            else if (!ws.isOpen()) {
//                Log.e(TAG, "reconnect socket");
//                reconnect();
//            }
        }
    }

//    private void reconnect() {
//        try {
//            connectSocket();
//            if (ws != null) {
//                ws.recreate().connectAsynchronously();
//            }
//        } catch (IOException e) {
//            Log.e(TAG, "exception recreate " + e);
//        }
//    }

    @Override
    public void tryConnect() {
        connectSocket();
    }

    @Override
    public void sendFile(Message message, String file) {
//        int maxPayloadSize = ws.getMaxPayloadSize();
        List<Message> fileMessage = MessageUtils.createFileMessage(message, file, 4096);
        for (Message m : fileMessage) {
            Log.e("message", m.getCommand() + " " + m.getParams());
            sendBinaryMessage(mtb.transform(m));
        }
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
        ws.sendBinary(message);
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
