package com.peekaboo.presentation;

import android.test.mock.MockContext;

import com.peekaboo.domain.AccountUser;
import com.peekaboo.domain.UserMessageMapper;
import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.usecase.ConfirmUseCase;
import com.peekaboo.domain.usecase.SignUpUseCase;
import com.peekaboo.presentation.presenters.SignUpPresenter;
import com.peekaboo.presentation.views.ICredentialsView;
import com.peekaboo.presentation.views.ISignUpView;

import org.junit.Test;
import org.mockito.Mock;

import rx.Observable;
import rx.Subscriber;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

public class SignUpPresenterTest extends BasePresenterTest {
    MockContext context = new MockContext();
    @Mock
    private ISignUpView signUpView;
    @Mock
    private UserMessageMapper errorHandler;

    @Test
    public void whenSignUpSuccessThenShowConfirmDialogIsCalled() {
        SignUpPresenter signUpPresenter = new SignUpPresenter(new SignUpUseCaseSuccess(), new ConfirmUseCaseSuccess(), errorHandler);
        signUpPresenter.bind(signUpView);
        signUpPresenter.onSignUpButtonClick("aValidUsername", "aValid@mail", "aValidPassword", "aValidPassword");

        verify(signUpView, timeout(WAIT).times(1)).showConfirmDialog();
    }

    @Test
    public void whenSignUpSuccessThenShowConfirmDialogIsCalledAfterRebind() {
        SignUpPresenter signUpPresenter = new SignUpPresenter(new SignUpUseCaseSuccess(), new ConfirmUseCaseSuccess(), errorHandler);
        signUpPresenter.bind(signUpView);
        signUpPresenter.onSignUpButtonClick("aValidUsername", "aValid@mail", "aValidPassword", "aValidPassword");
        signUpPresenter.unbind();
        sleep(WAIT);
        signUpPresenter.bind(signUpView);

        verify(signUpView, timeout(WAIT).times(1)).showConfirmDialog();
    }

    @Test
    public void whenSignUpExternalErrorThenShowConfirmDialogIsCalled() {
        SignUpPresenter signUpPresenter = new SignUpPresenter(new SignUpUseCaseFailure(), new ConfirmUseCaseSuccess(), errorHandler);
        signUpPresenter.bind(signUpView);
        signUpPresenter.onSignUpButtonClick("aValidUsername", "aValid@mail", "aValidPassword", "aValidPassword");

        verify(signUpView, timeout(WAIT).times(0)).showConfirmDialog();
        verify(signUpView, timeout(WAIT).times(1)).showToastMessage(any(String.class));
    }

    @Test
    public void whenInvalidUsernameThenShowInputErrorIsCalled() {
        SignUpPresenter signUpPresenter = new SignUpPresenter(new SignUpUseCaseSuccess(), new ConfirmUseCaseSuccess(), errorHandler);
        signUpPresenter.bind(signUpView);
        signUpPresenter.onSignUpButtonClick("shor", "aValid@mail", "aValidPassword", "aValidPassword");

        verify(signUpView, timeout(WAIT).times(0)).showConfirmDialog();
        verify(signUpView, timeout(WAIT).times(1)).showInputError(ICredentialsView.InputFieldError.USERNAME);
    }

    @Test
    public void whenInvalidLoginThenShowInputErrorIsCalled() {
        SignUpPresenter signUpPresenter = new SignUpPresenter(new SignUpUseCaseSuccess(), new ConfirmUseCaseSuccess(), errorHandler);
        signUpPresenter.bind(signUpView);
        signUpPresenter.onSignUpButtonClick("aValidUsername", "anInvalidLogin", "aValidPassword", "aValidPassword");

        verify(signUpView, timeout(WAIT).times(0)).showConfirmDialog();
        verify(signUpView, timeout(WAIT).times(1)).showInputError(ICredentialsView.InputFieldError.LOGIN);
    }

    @Test
    public void whenInvalidPasswordThenShowInputErrorIsCalled() {
        SignUpPresenter signUpPresenter = new SignUpPresenter(new SignUpUseCaseSuccess(), new ConfirmUseCaseSuccess(), errorHandler);
        signUpPresenter.bind(signUpView);
        signUpPresenter.onSignUpButtonClick("aValidUsername", "aValid@mail", "short", "aValidPassword");

        verify(signUpView, timeout(WAIT).times(0)).showConfirmDialog();
        verify(signUpView, timeout(WAIT).times(1)).showInputError(ICredentialsView.InputFieldError.PASSWORD);
    }

    @Test
    public void whenPasswordNotConfirmedThenShowInputErrorIsCalled() {
        SignUpPresenter signUpPresenter = new SignUpPresenter(new SignUpUseCaseSuccess(), new ConfirmUseCaseSuccess(), errorHandler);
        signUpPresenter.bind(signUpView);
        signUpPresenter.onSignUpButtonClick("aValidUsername", "aValid@mail", "aValidPassword", "notConfirmingPassword");

        verify(signUpView, timeout(WAIT).times(0)).showConfirmDialog();
        verify(signUpView, timeout(WAIT).times(1)).showInputError(ICredentialsView.InputFieldError.PASSWORD_CONFIRM);
    }

    @Test
    public void whenConfirmSuccessThenNavigateToProfile() {
        ConfirmUseCaseSuccess confirmUseCase = new ConfirmUseCaseSuccess();
        confirmUseCase.setUserId("id");
        SignUpPresenter signUpPresenter = new SignUpPresenter(new SignUpUseCaseSuccess(), confirmUseCase, errorHandler);
        signUpPresenter.bind(signUpView);
        signUpPresenter.onCodeConfirmButtonClick("1234");

        verify(signUpView, timeout(WAIT).times(1)).navigateToProfile();
        verify(signUpView, timeout(WAIT).times(1)).dismissConfirmDialog();
    }

    @Test
    public void whenConfirmSuccessThenNavigateToProfileAfterRebind() {
        ConfirmUseCaseSuccess confirmUseCase = new ConfirmUseCaseSuccess();
        confirmUseCase.setUserId("id");
        SignUpPresenter signUpPresenter = new SignUpPresenter(new SignUpUseCaseSuccess(), confirmUseCase, errorHandler);
        signUpPresenter.bind(signUpView);
        signUpPresenter.onCodeConfirmButtonClick("1234");
        signUpPresenter.unbind();
        sleep(WAIT);
        signUpPresenter.bind(signUpView);

        verify(signUpView, timeout(WAIT).times(1)).navigateToProfile();
        verify(signUpView, timeout(WAIT).times(1)).dismissConfirmDialog();
    }

    @Test
    public void whenInvalidCodeThenShowError() {
        ConfirmUseCaseSuccess confirmUseCase = new ConfirmUseCaseSuccess();
        confirmUseCase.setUserId("id");
        SignUpPresenter signUpPresenter = new SignUpPresenter(new SignUpUseCaseSuccess(), confirmUseCase, errorHandler);
        signUpPresenter.bind(signUpView);
        signUpPresenter.onCodeConfirmButtonClick("123");

        verify(signUpView, timeout(WAIT).times(1)).showToastMessage(any(String.class));
        verify(signUpView, timeout(WAIT).times(0)).navigateToProfile();
        verify(signUpView, timeout(WAIT).times(0)).dismissConfirmDialog();
    }

    @Test
    public void whenExternalFailureThenShowError() {
        ConfirmUseCaseFailure confirmUseCase = new ConfirmUseCaseFailure();
        confirmUseCase.setUserId("id");
        SignUpPresenter signUpPresenter = new SignUpPresenter(new SignUpUseCaseSuccess(), confirmUseCase, errorHandler);
        signUpPresenter.bind(signUpView);
        signUpPresenter.onCodeConfirmButtonClick("1234");

        verify(signUpView, timeout(WAIT).times(1)).showToastMessage(any(String.class));
        verify(signUpView, timeout(WAIT).times(0)).navigateToProfile();
    }


    private class SignUpUseCaseSuccess extends SignUpUseCase {

        public SignUpUseCaseSuccess() {
            super(mock(SessionRepository.class), subscribeOn, observeOn);
        }

        @Override
        protected Observable<AccountUser> getUseCaseObservable() {
            return Observable.just(new AccountUser("asd"));
        }
    }

    private class SignUpUseCaseFailure extends SignUpUseCase {

        public SignUpUseCaseFailure() {
            super(mock(SessionRepository.class), subscribeOn, observeOn);
        }

        @Override
        protected Observable<AccountUser> getUseCaseObservable() {
            return Observable.create(new Observable.OnSubscribe<AccountUser>() {
                @Override
                public void call(Subscriber<? super AccountUser> subscriber) {
                    subscriber.onError(new RuntimeException("Not great"));
                }
            });
        }
    }

    private class ConfirmUseCaseSuccess extends ConfirmUseCase {

        public ConfirmUseCaseSuccess() {
            super(mock(SessionRepository.class), subscribeOn, observeOn);
        }

        @Override
        protected Observable<AccountUser> getUseCaseObservable() {
            return Observable.just(mock(AccountUser.class));
        }
    }

    private class ConfirmUseCaseFailure extends ConfirmUseCase {

        public ConfirmUseCaseFailure() {
            super(mock(SessionRepository.class), subscribeOn, observeOn);
        }

        @Override
        protected Observable<AccountUser> getUseCaseObservable() {
            return Observable.create(new Observable.OnSubscribe<AccountUser>() {
                @Override
                public void call(Subscriber<? super AccountUser> subscriber) {
                    subscriber.onError(new RuntimeException("Not great"));
                }
            });
        }
    }

}
