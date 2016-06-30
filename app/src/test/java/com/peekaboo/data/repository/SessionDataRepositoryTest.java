package com.peekaboo.data.repository;

import com.peekaboo.data.mappers.AbstractMapperFactory;
import com.peekaboo.data.mappers.Mapper;
import com.peekaboo.data.mappers.MapperFactory;
import com.peekaboo.data.repositories.SessionDataRepository;
import com.peekaboo.data.rest.RestApi;
import com.peekaboo.data.rest.entity.UserEntity;
import com.peekaboo.domain.User;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import rx.Observable;
import rx.observers.TestSubscriber;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by sebastian on 29.06.16.
 */
public class SessionDataRepositoryTest {
    @Mock
    private RestApi restApi;
    @Mock
    private AbstractMapperFactory mapperFactory;
    private SessionDataRepository sessionDataRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sessionDataRepository = new SessionDataRepository(restApi, mapperFactory);
    }

    @Test
    public void whenLoginWithValidInputsThenGetCredentials() {
        when(restApi.login(any(String.class), any(String.class))).thenReturn(Observable.just(new UserEntity()));
        when(mapperFactory.getUserMapper()).thenReturn(obj -> new User("1"));

        TestSubscriber<User> subscriber = new TestSubscriber<>();
        sessionDataRepository.askForUser("username", "password").subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        assertEquals(subscriber.getOnNextEvents().size(), 1);
    }

    @Test
    public void whenLoginWithInvalidInputsThenCredentialsIsNotSaved() {
        when(restApi.login(any(String.class), any(String.class))).thenReturn(Observable.create(
                subscriber -> {
                    subscriber.onError(new RuntimeException("Not Great"));
                })
        );
        when(mapperFactory.getUserMapper()).thenReturn(obj -> new User("1"));

        TestSubscriber<User> subscriber = new TestSubscriber<>();
        sessionDataRepository.askForUser("username", "password").subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        assertEquals(subscriber.getOnErrorEvents().size(), 1);
    }
}
