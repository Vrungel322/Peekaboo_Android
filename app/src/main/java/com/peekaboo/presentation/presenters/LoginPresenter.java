package com.peekaboo.presentation.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

import com.peekaboo.domain.User;
import com.peekaboo.domain.subscribers.BaseProgressSubscriber;
import com.peekaboo.domain.usecase.LoginUseCase;
import com.peekaboo.presentation.views.ICredentialsView;
import com.peekaboo.utils.InternetBroadcastReceiver;
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
    private Context mContext;
    private IntentFilter ifInternetCheck;
    private InternetBroadcastReceiver ibrInternetCheck;

    @Inject
    public LoginPresenter(Context context, LoginUseCase useCase) {
        super(context);
        this.useCase = useCase;
        this.mContext = context;
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

    public void getFingerprint() {
        String[] fingerprints = VKUtil.getCertificateFingerprint(mContext, mContext.getPackageName());
        for (int i = 0; i < fingerprints.length; i++){
            Log.wtf("fingerprint", fingerprints[i]);
        }
    }
}
