package com.peekaboo.socket;

import com.peekaboo.data.Constants;
import com.peekaboo.data.mappers.MapperFactory;
import com.peekaboo.domain.User;
import com.peekaboo.presentation.services.INotifier;
import com.peekaboo.presentation.services.Message;
import com.peekaboo.presentation.services.WebSocketNotifier;

import org.junit.Test;
import org.mockito.Mock;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

public class NotifierTest {
//    @Mock
//    INotifier.NotificationListener listener;
//    @Test
//    public void whenSendMessage() throws Exception {
//        MockWebServer server = new MockWebServer();
//        server.enqueue(new MockResponse().setBody("hello, world!"));
//        server.url(Constants.BASE_URL_SOCKET);
//        System.out.println(server.getHostName());
//
//        server.start();
//
//        HttpUrl baseUrl = server.url(Constants.BASE_URL_SOCKET);
//        WebSocketNotifier webSocketNotifier = new WebSocketNotifier(new User("id"), new MapperFactory());
//        webSocketNotifier.addListener(listener);
//        webSocketNotifier.tryConnect();
//        sleep(500);
//        verify(listener, timeout(200).times(1)).onMessageObtained(any(Message.class));
//
//
//    }
//
//    private void sleep(int millis) throws InterruptedException {
//        Thread.sleep(millis);
//    }
}
