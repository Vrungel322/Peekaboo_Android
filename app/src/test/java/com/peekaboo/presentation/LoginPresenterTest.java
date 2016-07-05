package com.peekaboo.presentation;

import android.content.Context;
import android.test.mock.MockContext;

import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.User;
import com.peekaboo.domain.usecase.LoginUseCase;
<<<<<<< Updated upstream
import com.peekaboo.presentation.presenters.LoginPresenter;
import com.peekaboo.presentation.views.ICredentialsView;
=======
import com.peekaboo.presentation.BasePresenterTest;
import com.peekaboo.presentation.presenters.LoginPresenter;
import com.peekaboo.presentation.views.ILoginView;
>>>>>>> Stashed changes

import org.junit.Test;
import org.mockito.Mock;

import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

/**
 * Created by sebastian on 29.06.16.
 */
public class LoginPresenterTest extends BasePresenterTest {
    @Mock
<<<<<<< Updated upstream
    private ICredentialsView loginView;
=======
    private ILoginView loginView;
>>>>>>> Stashed changes
    @Mock
    private Context context;

    @Test
    public void whenLoginSuccessThenGoToProfileIsCalled() {
        LoginPresenter loginPresenter = new LoginPresenter(context, new TestLoginUseCaseSuccess());
        loginPresenter.bind(loginView);
        loginPresenter.onSignInButtonClick("username", "password");

        verify(loginView, timeout(WAIT).times(1)).navigateToProfile();
    }

    @Test
    public void whenLoginFailureThenGoToProfileIsNotCalled() {
        LoginPresenter loginPresenter = new LoginPresenter(new MockContext(), new TestLoginUseCaseFailure());
        loginPresenter.bind(loginView);
        loginPresenter.onSignInButtonClick("username", "password");

        verify(loginView, timeout(WAIT).times(0)).navigateToProfile();
        verify(loginView, timeout(WAIT).times(1)).onError(any(String.class));
    }

    class TestLoginUseCaseSuccess extends LoginUseCase {

        public TestLoginUseCaseSuccess() {
            super(mock(SessionRepository.class), subscribeOn, observeOn);
        }

        @Override
        protected Observable<User> getUseCaseObservable() {
            return Observable.just(new User("1"));
        }
    }

    class TestLoginUseCaseFailure extends LoginUseCase {

        public TestLoginUseCaseFailure() {
            super(mock(SessionRepository.class), subscribeOn, observeOn);
        }

        @Override
        protected Observable<User> getUseCaseObservable() {
            return Observable.create(subscriber -> {
                subscriber.onError(new RuntimeException("Not great"));
            });
        }
    }

}
