package com.peekaboo.domain.usecase;

import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.User;

import org.junit.Test;
import org.mockito.Mock;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

public class LogInUseCaseTest extends BaseUseCaseTest {
    private static final String A_VALID_USERNAME = "aValidUsername";
    private static final String A_VALID_PASSWORD = "aValidPassword";
    private static final String AN_INVALID_USERNAME = "anInvalidUsername";
    private static final String AN_INVALID_PASSWORD = "anInvalidPassword";
    @Mock
    private SessionRepository sessionRepository;
    private LoginUseCase loginUseCase;

    @Override
    public void setUp() {
        super.setUp();
        loginUseCase = new LoginUseCase(sessionRepository, subscribeOn, observeOn);

        when(sessionRepository.login(A_VALID_USERNAME, A_VALID_PASSWORD)).thenReturn(Observable.just(new User("")));
        when(sessionRepository.login(AN_INVALID_USERNAME, AN_INVALID_PASSWORD)).thenReturn(Observable.create(
                subscriber -> {
                    subscriber.onError(new RuntimeException("Not Great"));
                })
        );
    }

    @Test
    public void whenLoginSuccessThenReturnUser() {
        loginUseCase.setCredentials(A_VALID_USERNAME, A_VALID_PASSWORD);
        execute(0, 1);
    }

    @Test
    public void whenLoginFailureThenReturnError() {
        loginUseCase.setCredentials(AN_INVALID_USERNAME, AN_INVALID_PASSWORD);
        execute(1, 0);
    }

    private void execute(int expectedErrors, int expectedEvents) {
        TestSubscriber<User> subscriber = new TestSubscriber<>();
        loginUseCase.execute(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnErrorEvents().size(), is(expectedErrors));
        assertThat(subscriber.getOnNextEvents().size(), is(expectedEvents));
    }
}
