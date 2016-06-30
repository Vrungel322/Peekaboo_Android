package com.peekaboo.data.rest;

import com.peekaboo.data.rest.entity.UserEntity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.sql.RowSet;

import rx.Observable;
import rx.observers.TestSubscriber;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by sebastian on 29.06.16.
 */
public class RestApiTest {

    @Mock
    private PeekabooApi peekabooApi;
    private RestApi restApi;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        restApi = new RestApi(peekabooApi);
    }

    @Test
    public void whenLoginSuccess() {
        when(peekabooApi.login(any(String.class), any(String.class))).thenReturn(Observable.just(new UserEntity()));
        TestSubscriber<UserEntity> subscriber = new TestSubscriber<>();
        restApi.login("a", "aa").subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertEquals(subscriber.getOnNextEvents().size(), 1);
    }


    @Test
    public void whenLoginFailure() {
        when(peekabooApi.login(any(String.class), any(String.class))).thenReturn(Observable.create(
                subscriber -> {
                    subscriber.onError(new RuntimeException("Not Great"));
                }));
        TestSubscriber<UserEntity> subscriber = new TestSubscriber<>();
        restApi.login("a", "aa").subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertEquals(subscriber.getOnErrorEvents().size(), 1);
    }
}
