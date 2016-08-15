package com.peekaboo.domain.usecase;

import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.AccountUser;

import org.junit.Test;
import org.mockito.Mock;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

public class ConfirmUseCaseTest extends BaseUseCaseTest {
    private static final String A_VALID_ID = "aValidUsername";
    private static final String A_VALID_KEY = "aValidPassword";
    private static final String AN_INVALID_ID = "anInvalidUsername";
    private static final String AN_INVALID_KEY = "anInvalidPassword";
    @Mock
    private SessionRepository sessionRepository;
    private ConfirmUseCase confirmUseCase;

    @Override
    public void setUp() {
        super.setUp();
        confirmUseCase = new ConfirmUseCase(sessionRepository, subscribeOn, observeOn);

        when(sessionRepository.confirm(A_VALID_ID, A_VALID_KEY)).thenReturn(Observable.just(new AccountUser("")));
        when(sessionRepository.confirm(AN_INVALID_ID, AN_INVALID_KEY)).thenReturn(Observable.create(
                subscriber -> {
                    subscriber.onError(new RuntimeException("Not Great"));
                })
        );
    }

    @Test
    public void whenLoginSuccessThenReturnUser() {
        confirmUseCase.setUserId(A_VALID_ID);
        confirmUseCase.setConfirmKey(A_VALID_KEY);
        execute(0, 1);
    }

    @Test
    public void whenLoginFailureThenReturnError() {
        confirmUseCase.setUserId(AN_INVALID_ID);
        confirmUseCase.setConfirmKey(AN_INVALID_KEY);
        execute(1, 0);
    }

    private void execute(int expectedErrors, int expectedEvents) {
        TestSubscriber<AccountUser> subscriber = new TestSubscriber<>();
        confirmUseCase.execute(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnErrorEvents().size(), is(expectedErrors));
        assertThat(subscriber.getOnNextEvents().size(), is(expectedEvents));
    }
}
