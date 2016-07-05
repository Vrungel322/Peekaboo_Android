package com.peekaboo.presentation.presenters;

import android.content.Context;
import android.util.Log;

import com.peekaboo.domain.User;
import com.peekaboo.domain.subscribers.BaseProgressSubscriber;
import com.peekaboo.domain.usecase.LoginUseCase;
import com.peekaboo.domain.usecase.SignUpUseCase;
import com.peekaboo.presentation.views.ICredentialsView;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sebastian on 28.06.16.
 */
@Singleton
public class SignUpPresenter extends ProgressPresenter<ICredentialsView> implements ISignUpPresenter,
        BaseProgressSubscriber.ProgressSubscriberListener {

    private SignUpUseCase useCase;

    @Inject
    public SignUpPresenter(Context context, SignUpUseCase useCase) {
        super(context);
        this.useCase = useCase;
    }

    @Override
    public void onSignUpButtonClick(String login, String password, String email) {
        useCase.setCredentials(login, password, email);
        useCase.execute(new BaseProgressSubscriber<User>(this) {
            @Override
            public void onNext(User response) {
                super.onNext(response);
                Log.e("onNext", String.valueOf(response));
                if (getView() != null) {
                    getView().navigateToProfile();
                }
            }
        });
    }
}
