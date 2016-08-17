package com.peekaboo.presentation.presenters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.peekaboo.domain.AccountUser;
import com.peekaboo.domain.ErrorHandler;
import com.peekaboo.domain.subscribers.BaseProgressSubscriber;
import com.peekaboo.domain.usecase.LoginUseCase;
import com.peekaboo.presentation.utils.CredentialUtils;
import com.peekaboo.presentation.views.ICredentialsView;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.util.VKUtil;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sebastian on 28.06.16.
 */
@Singleton
public class LoginPresenter extends ProgressPresenter<ICredentialsView> implements ILoginPresenter,
        BaseProgressSubscriber.ProgressSubscriberListener {

    private LoginUseCase useCase;

    @Inject
    public LoginPresenter(Context context, LoginUseCase useCase, ErrorHandler errorHandler) {
        super(context, errorHandler);
        this.useCase = useCase;
    }

    @Override
    public void onSignInButtonClick(String login, String password) {
        if (isValid(login, password)) {
            useCase.setCredentials(login, password);
            useCase.execute(getSignInSubscriber());
        }
    }

    @NonNull
    private BaseProgressSubscriber<AccountUser> getSignInSubscriber() {
        return new BaseProgressSubscriber<AccountUser>(this) {
            @Override
            public void onNext(AccountUser response) {
                super.onNext(response);
                Log.e("onNext", String.valueOf(response));
                if (getView() != null) {
                    getView().navigateToProfile();
                }
            }
        };
    }

    private boolean isValid(String login, String password) {

        if (!(CredentialUtils.isUsernameValid(login)
                || CredentialUtils.isEmailValid(login)
                || CredentialUtils.isPhoneNumberValid(login))) {
            if (getView() != null) getView().showInputError(ICredentialsView.InputFieldError.LOGIN);
        } else if (!CredentialUtils.isPasswordValid(password)) {
            if (getView() != null) getView().showInputError(ICredentialsView.InputFieldError.PASSWORD);
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void onVkButtonClick() {
        String[] scope = new String[]{VKScope.MESSAGES,
                VKScope.FRIENDS,
                VKScope.WALL};
        VKSdk.login((Activity) getView(), scope);
    }

    @Override
    public void unbind() {
        useCase.unsubscribe();
        super.unbind();
    }

    @Override
    public void bind(ICredentialsView view) {
        super.bind(view);
        if (useCase.isWorking()) {
            useCase.execute(getSignInSubscriber());
        }
    }

    public void getFingerprint() {
        String[] fingerprints = VKUtil.getCertificateFingerprint(getContext(), getContext().getPackageName());
        for (int i = 0; i < fingerprints.length; i++){
            Log.wtf("fingerprint", fingerprints[i]);
        }
    }
}
