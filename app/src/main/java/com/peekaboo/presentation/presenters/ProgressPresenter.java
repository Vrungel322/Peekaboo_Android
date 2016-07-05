package com.peekaboo.presentation.presenters;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

import com.peekaboo.domain.User;
import com.peekaboo.domain.subscribers.BaseProgressSubscriber;
import com.peekaboo.domain.usecase.LoginUseCase;
import com.peekaboo.presentation.views.ICredentialsView;
import com.peekaboo.presentation.views.IProgressView;
import com.peekaboo.utils.InternetBroadcastReceiver;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sebastian on 28.06.16.
 */
public class ProgressPresenter<V extends IProgressView> extends BasePresenter<V> implements
        BaseProgressSubscriber.ProgressSubscriberListener {

    private Context mContext;
    private IntentFilter ifInternetCheck;
    private InternetBroadcastReceiver ibrInternetCheck;

    public ProgressPresenter(Context context) {
        mContext = context;
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
