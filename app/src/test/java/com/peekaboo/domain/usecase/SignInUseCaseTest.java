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

public class SignInUseCaseTest extends BaseUseCaseTest {
    private static final String A_VALID_USERNAME = "aValidUsername";
    private static final String A_VALID_LOGIN = "aValidLogin";
    private static final String A_VALID_PASSWORD = "aValidPassword";

    private static final String AN_INVALID_USERNAME = "anInvalidUsername";
    private static final String AN_INVALID_LOGIN = "anInvalidLogin";
    private static final String AN_INVALID_PASSWORD = "anInvalidPassword";

    @Mock
    private SessionRepository sessionRepository;
    private SignUpUseCase signUpUseCase;

    @Override
    public void setUp() {
        super.setUp();
        signUpUseCase = new SignUpUseCase(sessionRepository, subscribeOn, observeOn);

        when(sessionRepository.signUp(A_VALID_USERNAME, A_VALID_LOGIN, A_VALID_PASSWORD)).thenReturn(Observable.just(new AccountUser("")));
        when(sessionRepository.signUp(AN_INVALID_USERNAME, AN_INVALID_LOGIN, AN_INVALID_PASSWORD)).thenReturn(Observable.create(
                subscriber -> {
                    subscriber.onError(new RuntimeException("Not Great"));
                })
        );
    }

    @Test
    public void whenSignUpSuccessThenReturnUser() {
        signUpUseCase.setCredentials(A_VALID_USERNAME, A_VALID_LOGIN, A_VALID_PASSWORD);
        execute(0, 1);
    }

    @Test
    public void whenSignUpFailureThenReturnError() {
        signUpUseCase.setCredentials(AN_INVALID_USERNAME, AN_INVALID_LOGIN, AN_INVALID_PASSWORD);
        execute(1, 0);
    }

    private void execute(int expectedErrors, int expectedEvents) {
        TestSubscriber<AccountUser> subscriber = new TestSubscriber<>();
        signUpUseCase.execute(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnErrorEvents().size(), is(expectedErrors));
        assertThat(subscriber.getOnNextEvents().size(), is(expectedEvents));
    }
}
