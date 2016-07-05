package com.peekaboo.presentation.presenters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.peekaboo.domain.User;
import com.peekaboo.domain.subscribers.BaseProgressSubscriber;
import com.peekaboo.domain.usecase.LoginUseCase;
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
    public LoginPresenter(Context context, LoginUseCase useCase) {
        super(context);
        this.useCase = useCase;
    }

    @Override
    public void onSignInButtonClick(String login, String password) {
        useCase.setCredentials(login, password);
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

    @Override
    public void onVkButtonClick() {
        String[] scope = new String[]{VKScope.MESSAGES,
                VKScope.FRIENDS,
                VKScope.WALL};
        VKSdk.login((Activity) getView(), scope);
    }

    public void getFingerprint() {
        String[] fingerprints = VKUtil.getCertificateFingerprint(getContext(), getContext().getPackageName());
        for (int i = 0; i < fingerprints.length; i++){
            Log.wtf("fingerprint", fingerprints[i]);
        }
    }
}
