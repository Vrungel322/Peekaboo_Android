package com.peekaboo.presentation.presenters;

import com.peekaboo.R;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.domain.UserMessageMapper;
import com.peekaboo.domain.subscribers.BaseProgressSubscriber;
import com.peekaboo.domain.usecase.ConfirmUseCase;
import com.peekaboo.domain.usecase.SignUpUseCase;
import com.peekaboo.presentation.utils.CredentialUtils;
import com.peekaboo.presentation.views.ICredentialsView;
import com.peekaboo.presentation.views.ISignUpView;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sebastian on 28.06.16.
 */
@Singleton
public class SignUpPresenter extends ProgressPresenter<ISignUpView> implements ISignUpPresenter,
        BaseProgressSubscriber.ProgressSubscriberListener {
    private SignUpUseCase signUpUseCase;
    private ConfirmUseCase confirmUseCase;

    @Inject
    public SignUpPresenter(SignUpUseCase signUpUseCase, ConfirmUseCase confirmUseCase,
                           UserMessageMapper errorHandler) {
        super(errorHandler);
        this.signUpUseCase = signUpUseCase;
        this.confirmUseCase = confirmUseCase;
    }

    public BaseProgressSubscriber<AccountUser> getSignUpSubscriber() {
        return new BaseProgressSubscriber<AccountUser>(this) {
            @Override
            public void onNext(AccountUser response) {
                super.onNext(response);
                confirmUseCase.setUserId(response.getId());
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                if (getView() != null)
                    getView().showConfirmDialog();
            }
        };
    }

    public BaseProgressSubscriber<AccountUser> getConfirmSubscriber() {
        return new BaseProgressSubscriber<AccountUser>(this) {
            @Override
            public void onCompleted() {
                super.onCompleted();
                ISignUpView view = getView();
                if (view != null) {
                    view.navigateToProfile();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ISignUpView view = getView();
                if (view != null) {
                    view.showConfirmDialog();

                }
            }
        };
    }

    @Override
    public void onSignUpButtonClick(String phone, String username, String login, String password, String passwordConfirm) {
        if (isValid(username, login, password, passwordConfirm)) {
            signUpUseCase.setCredentials(phone, username, login, password);
            signUpUseCase.execute(getSignUpSubscriber());
        }
    }

    @Override
    public void onCodeConfirmButtonClick(String key) {
        if (isValid(key)) {
            confirmUseCase.setConfirmKey(key);
            confirmUseCase.execute(getConfirmSubscriber());
            ISignUpView view = getView();
            if (view != null) {
                view.dismissConfirmDialog();
            }
        }
    }

    private boolean isValid(String key) {
        if (key.contains(" ") || key.length() != 4) {
            showMessage(R.string.invalidKey);
        } else {
            return true;
        }
        return false;
    }

    private boolean isValid(String username, String login, String password, String passwordConfirm) {
        ISignUpView view = getView();
        if (!(CredentialUtils.isEmailValid(login) || CredentialUtils.isPhoneNumberValid(login))) {
            if (view != null) view.showInputError(ICredentialsView.InputFieldError.LOGIN);
        } else if (!CredentialUtils.isPasswordValid(password)) {
            if (view != null)
                view.showInputError(ICredentialsView.InputFieldError.PASSWORD);
        } else if (!CredentialUtils.isPasswordConfirmed(password, passwordConfirm)) {
            if (view != null)
                view.showInputError(ICredentialsView.InputFieldError.PASSWORD_CONFIRM);
        } else if (!CredentialUtils.isUsernameValid(username)) {
            if (view != null)
                view.showInputError(ICredentialsView.InputFieldError.USERNAME);
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void unbind() {
        signUpUseCase.unsubscribe();
        confirmUseCase.unsubscribe();
        super.unbind();
    }

    @Override
    public void bind(ISignUpView view) {
        super.bind(view);
        if (signUpUseCase.isWorking()) {
            signUpUseCase.execute(getSignUpSubscriber());
        }
        if (confirmUseCase.isWorking()) {
            confirmUseCase.execute(getConfirmSubscriber());
        }
    }
}
