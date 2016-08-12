package com.peekaboo.data;

import com.peekaboo.data.mappers.AbstractMapperFactory;
import com.peekaboo.data.repositories.SessionDataRepository;
import com.peekaboo.data.rest.ConfirmKey;
import com.peekaboo.data.rest.RestApi;
import com.peekaboo.data.rest.entity.Credentials;
import com.peekaboo.data.rest.entity.CredentialsSignUp;
import com.peekaboo.data.rest.entity.TokenEntity;
import com.peekaboo.domain.AccountUser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SessionDataRepositoryTest {
    public static final String TOKEN = "token";
    public static final String ID = "id";
    @Mock
    private RestApi restApi;
    @Mock
    private AccountUser user;
    @Mock
    private AbstractMapperFactory mapper;

    private SessionDataRepository sessionDataRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sessionDataRepository = new SessionDataRepository(restApi, mapper, user);
    }

    @Test
    public void whenLoginSuccessThenReturnUser() {
        when(restApi.login(any(Credentials.class))).thenReturn(Observable.just(new TokenEntity(TOKEN, ID)));
        TestSubscriber<AccountUser> subscriber = new TestSubscriber<>();
        sessionDataRepository.login("username", "password").subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnNextEvents().size(), is(1));
        verify(user, times(1)).saveId(ID);
        verify(user, times(1)).saveToken(TOKEN);
    }

    @Test
    public void whenLoginFailureThenReturnError() {
        when(restApi.login(any(Credentials.class))).thenReturn(Observable.create(
                subscriber -> {
                    subscriber.onError(new RuntimeException("Not Great"));
                }));
        TestSubscriber<AccountUser> subscriber = new TestSubscriber<>();
        sessionDataRepository.login("username", "password").subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnErrorEvents().size(), is(1));
        verify(user, times(0)).saveId(ID);
        verify(user, times(0)).saveToken(TOKEN);
    }

    @Test
    public void whenSignUpSuccessThenReturnUser() {
        when(restApi.signUp(any(CredentialsSignUp.class))).thenReturn(Observable.just(new TokenEntity(null, ID)));
        TestSubscriber<AccountUser> subscriber = new TestSubscriber<>();
        sessionDataRepository.signUp("username", "login", "password").subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnNextEvents().size(), is(1));
        verify(user, times(1)).saveId(ID);
        verify(user, times(0)).saveToken(null);
    }

    @Test
    public void whenSignUpFailureThenReturnError() {
        when(restApi.signUp(any(CredentialsSignUp.class))).thenReturn(Observable.create(
                subscriber -> {
                    subscriber.onError(new RuntimeException("Not Great"));
                }));
        TestSubscriber<AccountUser> subscriber = new TestSubscriber<>();
        sessionDataRepository.signUp("username", "login", "password").subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnErrorEvents().size(), is(1));
        verify(user, times(0)).saveId(ID);
        verify(user, times(0)).saveToken(null);
    }

    @Test
    public void whenConfirmSuccessThenReturnUser() {
        when(restApi.confirm(any(ConfirmKey.class))).thenReturn(Observable.just(new TokenEntity(TOKEN, ID)));
        TestSubscriber<AccountUser> subscriber = new TestSubscriber<>();
        sessionDataRepository.confirm("id", "key").subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnNextEvents().size(), is(1));
        verify(user, times(1)).saveId(ID);
        verify(user, times(1)).saveToken(TOKEN);
    }

    @Test
    public void whenConfirmFailureThenReturnError() {
        when(restApi.confirm(any(ConfirmKey.class))).thenReturn(Observable.create(
                subscriber -> {
                    subscriber.onError(new RuntimeException("Not Great"));
                }));
        TestSubscriber<AccountUser> subscriber = new TestSubscriber<>();
        sessionDataRepository.confirm("id", "key").subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnErrorEvents().size(), is(1));
        verify(user, times(0)).saveId(ID);
        verify(user, times(0)).saveToken(TOKEN);
    }
}
