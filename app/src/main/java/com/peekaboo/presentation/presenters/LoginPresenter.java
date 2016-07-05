package com.peekaboo.presentation.presenters;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

import com.peekaboo.domain.User;
import com.peekaboo.domain.subscribers.BaseProgressSubscriber;
import com.peekaboo.domain.usecase.LoginUseCase;
import com.peekaboo.presentation.views.ICredentialsView;
import com.peekaboo.utils.InternetBroadcastReceiver;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sebastian on 28.06.16.
 */
@Singleton
public class LoginPresenter extends ProgressPresenter<ICredentialsView> implements ILoginPresenter,
        BaseProgressSubscriber.ProgressSubscriberListener {

    private LoginUseCase useCase;
<<<<<<< Updated upstream
=======
    private  Context mContext;
    private IntentFilter ifInternetCheck;
    private InternetBroadcastReceiver ibrInternetCheck;
@Singleton
public class LoginPresenter extends ProgressPresenter<ICredentialsView> implements ILoginPresenter,
        BaseProgressSubscriber.ProgressSubscriberListener {

    private LoginUseCase useCase;
>>>>>>> arkadii
>>>>>>> Stashed changes

    @Inject
    public LoginPresenter(Context context, LoginUseCase useCase) {
        super(context);
        this.useCase = useCase;
    }

    @Override
    public void onSignInButtonClick(String login, String password) {
        if (isValid)
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
}
