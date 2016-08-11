package com.peekaboo.presentation;

import android.test.mock.MockContext;

import com.peekaboo.domain.ErrorHandler;
import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.User;
import com.peekaboo.domain.usecase.LoginUseCase;
import com.peekaboo.presentation.presenters.LoginPresenter;
import com.peekaboo.presentation.views.ICredentialsView;

import org.junit.Test;
import org.mockito.Mock;

import rx.Observable;
import rx.Subscriber;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

/**
 * Created by sebastian on 16.07.16.
 */
public class LoginPresenterTest extends BasePresenterTest {
    MockContext context = new MockContext();
    @Mock
    private ICredentialsView loginView;
    @Mock
    private ErrorHandler errorHandler;

    @Test
    public void whenLoginSuccessThenNavigateToProfileIsCalled() {
        LoginPresenter loginPresenter = new LoginPresenter(context, new LoginUseCaseSuccess(), errorHandler);
        loginPresenter.bind(loginView);
        loginPresenter.onSignInButtonClick("aValidUsername", "aValidPassword");

        verify(loginView, timeout(WAIT).times(1)).navigateToProfile();
    }

    @Test
    public void whenLoginSuccessThenNavigateToProfileIsCalledAfterRebind() {
        LoginPresenter loginPresenter = new LoginPresenter(context, new LoginUseCaseSuccess(), errorHandler);
        loginPresenter.bind(loginView);
        loginPresenter.onSignInButtonClick("aValidUsername", "aValidPassword");
        loginPresenter.unbind();
        sleep(WAIT);
        loginPresenter.bind(loginView);

        verify(loginView, timeout(WAIT).times(1)).navigateToProfile();
    }

    @Test
    public void whenInvalidLoginThenShowInputErrorIsCalled() {
        LoginPresenter loginPresenter = new LoginPresenter(context, new LoginUseCaseSuccess(), errorHandler);
        loginPresenter.bind(loginView);
        loginPresenter.onSignInButtonClick("short", "aValidPassword");

        verify(loginView, timeout(WAIT).times(0)).navigateToProfile();
        verify(loginView, timeout(WAIT).times(1)).showInputError(ICredentialsView.InputFieldError.LOGIN);
    }


    @Test
    public void whenInvalidPasswordThenShowInputErrorIsCalled() {
        LoginPresenter loginPresenter = new LoginPresenter(context, new LoginUseCaseSuccess(), errorHandler);
        loginPresenter.bind(loginView);
        loginPresenter.onSignInButtonClick("aValidLogin", "short");

        verify(loginView, timeout(WAIT).times(0)).navigateToProfile();
        verify(loginView, timeout(WAIT).times(1)).showInputError(ICredentialsView.InputFieldError.PASSWORD);
    }

    @Test
    public void whenExternalErrorThenShowLoginErrorIsCalled() {
        LoginPresenter loginPresenter = new LoginPresenter(context, new LoginUseCaseFailure(), errorHandler);
        loginPresenter.bind(loginView);
        loginPresenter.onSignInButtonClick("aValidLogin", "aValidPassword");

        verify(loginView, timeout(WAIT).times(0)).navigateToProfile();
        verify(loginView, timeout(WAIT).times(1)).onError(any(String.class));
    }

    private class LoginUseCaseSuccess extends LoginUseCase {
        public LoginUseCaseSuccess() {
            super(mock(SessionRepository.class), subscribeOn, observeOn);
        }

        @Override
        protected Observable<User> getUseCaseObservable() {
            return Observable.just(mock(User.class));
        }
    }

    private class LoginUseCaseFailure extends LoginUseCase {
        public LoginUseCaseFailure() {
            super(mock(SessionRepository.class), subscribeOn, observeOn);
        }

        @Override
        protected Observable<User> getUseCaseObservable() {
            return Observable.create(new Observable.OnSubscribe<User>() {
                @Override
                public void call(Subscriber<? super User> subscriber) {
                    subscriber.onError(new RuntimeException("Not great"));
                }
            });
        }
    }

}
