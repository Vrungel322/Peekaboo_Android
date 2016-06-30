package com.peekaboo.domain;

import com.peekaboo.domain.usecase.LoginUseCase;

import org.junit.Test;
import org.mockito.Mock;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

/**
 * Created by sebastian on 29.06.16.
 */
public class LoginUseCaseTest extends BaseUseCaseTest {
    private static final String A_VALID_USERNAME = "a";
    private static final String AN_INVALID_USERNAME = "a";
    private static final String A_VALID_PASSWORD = "aa";
    private static final String AN_INVALID_PASSWORD = "a";
    @Mock
    private SessionRepository sessionRepository;
    private LoginUseCase loginUseCase;

    @Override
    public void setUp() {
        super.setUp();
        loginUseCase = new LoginUseCase(sessionRepository, subscribeOn, observeOn);

        when(sessionRepository.askForUser(A_VALID_USERNAME, A_VALID_PASSWORD)).thenReturn(Observable.just(new User("1")));
        when(sessionRepository.askForUser(AN_INVALID_USERNAME, AN_INVALID_PASSWORD)).thenReturn(Observable.create(
                (Observable.OnSubscribe<User>) subscriber -> {
                    subscriber.onError(new RuntimeException("Not Great"));
                })
        );
    }

    @Test
    public void whenLoginWithValidInputsThenGetA_ResponseAsString() {
        loginUseCase.setCredentials(A_VALID_USERNAME, A_VALID_PASSWORD);
        subscribeToLoginAndWaitUntilObservableIsCompleted(0, 1);
    }

    @Test
    public void whenLoginWithInvalidInputsThenThrowAnExceptionOnSubscriber() {
        loginUseCase.setCredentials(AN_INVALID_USERNAME, AN_INVALID_PASSWORD);
        subscribeToLoginAndWaitUntilObservableIsCompleted(1, 0);
    }

    private void subscribeToLoginAndWaitUntilObservableIsCompleted(int expectedErrors, int expectedEvents) {
        TestSubscriber<User> subscriber = new TestSubscriber<>();
        loginUseCase.execute(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnErrorEvents().size(), is(expectedErrors));
        assertThat(subscriber.getOnNextEvents().size(), is(expectedEvents));
    }
}
