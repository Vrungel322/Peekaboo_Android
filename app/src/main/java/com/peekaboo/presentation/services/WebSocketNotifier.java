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
import java.util.List;
import java.util.Map;

/**
 * Created by sebastian on 12.07.16.
 */

public class WebSocketNotifier implements INotifier {
    public static final String BASE_URL_SOCKET = Constants.BASE_URL_SOCKET;
    public static final int TIMEOUT = 5000;
    private final String TAG = "socket";

    private final User user;
    private final Mapper<Message, String> messageMapper;
    @Nullable
    private WebSocket ws;

    public WebSocketNotifier(User user, AbstractMapperFactory abstractMapperFactory) {
        this.user = user;
        messageMapper = abstractMapperFactory.getMessageMapper();
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
                                    if (ws != null) {
                                        ws.clearHeaders();
                                    }
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
            ws.sendText(messageMapper.transform(message));
        }
    }
}
