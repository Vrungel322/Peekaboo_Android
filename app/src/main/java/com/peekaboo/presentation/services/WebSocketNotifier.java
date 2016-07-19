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
import com.peekaboo.domain.User;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by sebastian on 12.07.16.
 */

public class WebSocketNotifier implements INotifier {
    public static final String BASE_URL_SOCKET = Constants.BASE_URL_SOCKET;
    public static final int TIMEOUT = 5000;
    private final String TAG = "socket";

    private final User user;
    private final Mapper<Message, String> messageToStringMapper;
    private final Mapper<String, Message> stringToMessageMapper;
    @Nullable
    private WebSocket ws;
    private Set<NotificationListener> listeners = new HashSet<>();

    public WebSocketNotifier(User user, AbstractMapperFactory abstractMapperFactory) {
        this.user = user;
        messageToStringMapper = abstractMapperFactory.getMessageToStringMapper();
        stringToMessageMapper = abstractMapperFactory.getStringToMessageMapper();
        connectSocket();
    }

    private void connectSocket() {
        if (user.isAuthorized()) {
            if (ws == null) {
                try {
                    Log.e(TAG, "create new socket");
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
                                public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                                    Log.e(TAG, "Status: Disconnected ");
                                    reconnect();
                                }

                                @Override
                                public void onTextMessage(WebSocket websocket, String text) throws Exception {
                                    Log.e(TAG, "Status: Text Message received" + text);
                                    for (NotificationListener listener : listeners) {
                                        listener.onMessageObtained(stringToMessageMapper.transform(text));
                                    }
                                }
                            })
                            .addHeader("Authorization", user.getBearer())
                            .connectAsynchronously();
                } catch (IOException e) {
                    Log.e(TAG, "exception " + e);
                }
            } else if (!ws.isOpen()) {
                Log.e(TAG, "reconnect socket");
                reconnect();
            }
        }
    }

    private void reconnect() {
        try {
            if (ws != null) {
                ws.recreate().connectAsynchronously();
            }
        } catch (IOException e) {
            Log.e(TAG, "exception recreate " + e);
        }
    }

    @Override
    public void tryConnect() {
        connectSocket();
    }

    @Override
    public boolean isAvailable() {
        return ws != null && ws.isOpen();
    }

    @Override
    public void sendMessage(Message message) {
        if (ws != null) {
            ws.sendText(messageToStringMapper.transform(message));
        }
    }

    public void addListener(NotificationListener listener) {
        listeners.add(listener);
    }

    public void removeListener(NotificationListener listener) {
        listeners.remove(listener);
    }
}
