package com.peekaboo.presentation.presenters;

import android.content.Context;

import com.peekaboo.R;
import com.peekaboo.domain.ErrorHandler;
import com.peekaboo.domain.User;
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
    private User user;

    @Inject
    public SignUpPresenter(Context context,
                           SignUpUseCase signUpUseCase, ConfirmUseCase confirmUseCase,
                           ErrorHandler errorHandler) {
        super(context, errorHandler);
        this.signUpUseCase = signUpUseCase;
        this.confirmUseCase = confirmUseCase;
    }

    public BaseProgressSubscriber<User> getSignUpSubscriber() {
        return new BaseProgressSubscriber<User>(this) {
            @Override
            public void onNext(User response) {
                super.onNext(response);
                user = response;
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                if (getView() != null)
                    getView().showConfirmDialog();
            }
        };
    }

    public BaseProgressSubscriber<User> getConfirmSubscriber() {
        return new BaseProgressSubscriber<User>(this) {
            @Override
            public void onCompleted() {
                super.onCompleted();
                if (getView() != null) {
                    getView().navigateToProfile();
                }
            }
        };
    }

    @Override
    public void onSignUpButtonClick(String username, String login, String password, String passwordConfirm) {
        if (isValid(username, login, password, passwordConfirm)) {
            signUpUseCase.setCredentials(username, login, password);
            signUpUseCase.execute(getSignUpSubscriber());

        }
    }

    @Override
    public void onCodeConfirmButtonClick(String key) {
        if (isValid(key)) {
            confirmUseCase.setConfirmData(user.getId(), key);
            confirmUseCase.execute(getConfirmSubscriber());
        }
    }

    private boolean isValid(String key) {
        if (key.contains(" ") || key.length() != 4) {
            if (getView() != null) getView().onError(getContext().getString(R.string.invalidKey));
        } else {
            return true;
        }
        return false;
    }

    private boolean isValid(String username, String login, String password, String passwordConfirm) {
        if (!(CredentialUtils.isEmailValid(login) || CredentialUtils.isPhoneNumberValid(login))) {
            if (getView() != null) getView().showInputError(ICredentialsView.InputFieldError.LOGIN);
        } else if (!CredentialUtils.isPasswordValid(password)) {
            if (getView() != null)
                getView().showInputError(ICredentialsView.InputFieldError.PASSWORD);
        } else if (!CredentialUtils.isPasswordConfirmed(password, passwordConfirm)) {
            if (getView() != null)
                getView().showInputError(ICredentialsView.InputFieldError.PASSWORD_CONFIRM);
        } else if (!CredentialUtils.isUsernameValid(username)) {
            if (getView() != null)
                getView().showInputError(ICredentialsView.InputFieldError.USERNAME);
        } else {
            return true;
        }
        return false;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void unbind() {
        super.unbind();
        if (signUpUseCase.isWorking()) {
            signUpUseCase.unsubscribe();
        } else if (confirmUseCase.isWorking()) {
            confirmUseCase.unsubscribe();
        }
    }

    @Override
    public void bind(ISignUpView view) {
        super.bind(view);
        if (signUpUseCase.isWorking()) {
            signUpUseCase.execute(getSignUpSubscriber());
        } else if (confirmUseCase.isWorking()) {
            confirmUseCase.execute(getConfirmSubscriber());
        }
    }
}
