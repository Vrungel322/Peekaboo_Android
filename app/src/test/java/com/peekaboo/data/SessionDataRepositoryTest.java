package com.peekaboo.data;

import android.content.ContentResolver;
import android.support.annotation.NonNull;

import com.peekaboo.data.mappers.AbstractMapperFactory;
import com.peekaboo.data.mappers.ContactEntityToContactMapper;
import com.peekaboo.data.repositories.SessionDataRepository;
import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.data.repositories.database.contacts.PContactHelper;
import com.peekaboo.data.repositories.database.messages.PMessageHelper;
import com.peekaboo.data.rest.ConfirmKey;
import com.peekaboo.data.rest.RestApi;
import com.peekaboo.data.rest.entity.ContactEntity;
import com.peekaboo.data.rest.entity.Credentials;
import com.peekaboo.data.rest.entity.CredentialsSignUp;
import com.peekaboo.data.rest.entity.TokenEntity;
import com.peekaboo.data.rest.entity.UserResponse;
import com.peekaboo.domain.AccountUser;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SessionDataRepositoryTest {
    private static final String TOKEN = "token";
    private static final String ID = "id";
    private static final String USERNAME = "username";
    private static final int MODE = 1;
    private static final String AVATAR_URL = "asd";
    @Mock
    private RestApi restApi;
    @Mock
    private AccountUser user;
    @Mock
    private AbstractMapperFactory mapper;
    @Mock
    PContactHelper contactHelper;
    @Mock
    PMessageHelper messageHelper;
    private SessionDataRepository sessionDataRepository;
    @Mock
    ContentResolver contentResolver;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sessionDataRepository = new SessionDataRepository(restApi, mapper, user, contactHelper, messageHelper, contentResolver);
    }

    @Test
    public void whenLoginSuccessThenReturnUser() {
        when(mapper.getContactEntityMapper()).thenReturn(new ContactEntityToContactMapper(AVATAR_URL));
        when(restApi.login(any(Credentials.class))).thenReturn(Observable.just(new TokenEntity(TOKEN, ID, MODE, USERNAME)));
        UserResponse value = new UserResponse();
        value.usersList = new ArrayList<>();
        ContactEntity e = new ContactEntity();
        e.setId(ID);

        value.usersList.add(e);
        when(restApi.getAllContacts()).thenReturn(Observable.just(value));

        TestSubscriber<AccountUser> subscriber = new TestSubscriber<>();
        sessionDataRepository.login("username", "password").subscribe(subscriber);
        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
        verify(user, times(1)).saveId(ID);
        verify(user, times(1)).saveToken(TOKEN);
        verify(user, times(1)).saveUsername(USERNAME);
        verify(user, times(1)).saveMode(MODE);
        verify(contactHelper, times(1)).insert(Mockito.argThat(getcontactMatcher()));
        verify(messageHelper, times(1)).createTable(ID);
    }

    @NonNull
    private BaseMatcher<Contact> getcontactMatcher() {
        return new BaseMatcher<Contact>() {
            @Override
            public boolean matches(Object o) {
                return o instanceof Contact && ((Contact) o).contactId().equals(ID);
            }

            @Override
            public void describeTo(Description description) {

            }
        };
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
        verify(user, times(0)).saveId(any());
        verify(user, times(0)).saveToken(any());
        verify(user, times(0)).saveUsername(any());
        verify(user, times(0)).saveMode(anyInt());
        verify(contactHelper, times(0)).insert(Mockito.argThat(getcontactMatcher()));
        verify(messageHelper, times(0)).createTable(ID);
    }

    @Test
    public void whenSignUpSuccessThenReturnUser() {
        TokenEntity value = new TokenEntity(null, ID, 0, USERNAME);
        when(restApi.signUp(any(CredentialsSignUp.class))).thenReturn(Observable.just(value));
        TestSubscriber<AccountUser> subscriber = new TestSubscriber<>();
        sessionDataRepository.signUp("123", "username", "login", "password").subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnNextEvents().size(), is(1));
        verify(user, times(1)).saveId(ID);
        verify(user, times(1)).saveUsername(USERNAME);
        verify(user, times(0)).saveToken(any());
        verify(user, times(0)).saveMode(anyInt());
    }

    @Test
    public void whenSignUpFailureThenReturnError() {
        when(restApi.signUp(any(CredentialsSignUp.class))).thenReturn(Observable.create(
                subscriber -> {
                    subscriber.onError(new RuntimeException("Not Great"));
                }));
        TestSubscriber<AccountUser> subscriber = new TestSubscriber<>();
        sessionDataRepository.signUp("123", "username", "login", "password").subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnErrorEvents().size(), is(1));
        verify(user, times(0)).saveId(any());
        verify(user, times(0)).saveToken(any());
        verify(user, times(0)).saveMode(anyInt());
        verify(user, times(0)).saveUsername(any());
    }

    @Test
    public void whenConfirmSuccessThenReturnUser() {
        when(mapper.getContactEntityMapper()).thenReturn(new ContactEntityToContactMapper(AVATAR_URL));
        when(restApi.confirm(any(ConfirmKey.class))).thenReturn(Observable.just(new TokenEntity(TOKEN, null, 0, null)));

        UserResponse value = new UserResponse();
        value.usersList = new ArrayList<>();
        ContactEntity e = new ContactEntity();
        e.setId(ID);
        value.usersList.add(e);
        when(restApi.getAllContacts()).thenReturn(Observable.just(value));

        TestSubscriber<AccountUser> subscriber = new TestSubscriber<>();
        sessionDataRepository.confirm("id", "key").subscribe(subscriber);
        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
        verify(user, times(0)).saveId(any());
        verify(user, times(0)).saveMode(anyInt());
        verify(user, times(0)).saveUsername(any());
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
        verify(user, times(0)).saveId(any());
        verify(user, times(0)).saveToken(any());
        verify(user, times(0)).saveUsername(any());
        verify(user, times(0)).saveMode(anyInt());
    }
}
