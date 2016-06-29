package com.peekaboo.presentation.presenters;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.peekaboo.domain.User;
import com.peekaboo.domain.subscribers.BaseProgressSubscriber;
import com.peekaboo.domain.usecase.LoginUseCase;
import com.peekaboo.presentation.views.ILoginView;
import com.peekaboo.utils.InternetBroadcastReceiver;

import javax.inject.Inject;

/**
 * Created by sebastian on 28.06.16.
 */
public class LoginPresenter extends BasePresenter<ILoginView> implements ILoginPresenter,
        BaseProgressSubscriber.ProgressSubscriberListener {

    private LoginUseCase useCase;
    private  Context mContext;
    private IntentFilter ifInternetCheck;
    private InternetBroadcastReceiver ibrInternetCheck;

    @Inject
    public LoginPresenter(Context context, LoginUseCase useCase) {
        this.useCase = useCase;
        mContext = context;
    }

    @Override
    public void onSignInButtonClick(String login, String password) {
        useCase.setCredentials(login, password);
        useCase.execute(new BaseProgressSubscriber<User>(this) {
            @Override
            public void onNext(User response) {
                super.onNext(response);
                if (getView() != null) {
                    getView().navigateToProfile();
                }
            }
        });
    }

    @Override
    public void onError(Throwable t) {
        if (getView() != null) {
            getView().hideProgress();
            getView().onError(t.getMessage());
        }
    }

    @Override
    public void onCompleted() {
        if (getView() != null) {
            getView().hideProgress();
        }
    }

    @Override
    public void onStartLoading() {
        if (getView() != null) {
            getView().showProgress();
        }
    }

    public void setCheckingInternet() {
        //set all stuff for checking Internet
        ibrInternetCheck = new InternetBroadcastReceiver();
        ifInternetCheck = new IntentFilter();
        ifInternetCheck.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        //start checking Internet connection
        mContext.registerReceiver(ibrInternetCheck, ifInternetCheck);
    }
}
