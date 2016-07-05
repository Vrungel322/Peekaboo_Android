package com.peekaboo.presentation.presenters;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.peekaboo.domain.ErrorHandler;
import com.peekaboo.domain.subscribers.BaseProgressSubscriber;
import com.peekaboo.presentation.views.IProgressView;
import com.peekaboo.utils.InternetBroadcastReceiver;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by sebastian on 28.06.16.
 */
public class ProgressPresenter<V extends IProgressView> extends BasePresenter<V> implements
        BaseProgressSubscriber.ProgressSubscriberListener {

    private Context mContext;
    private ErrorHandler errorHandler;
    private IntentFilter ifInternetCheck;
    private InternetBroadcastReceiver ibrInternetCheck;

    public ProgressPresenter(Context context, ErrorHandler errorHandler) {
        mContext = context;
        this.errorHandler = errorHandler;
    }

    @Override
    public void onError(Throwable t) {
        if (getView() != null) {
            getView().hideProgress();
            getView().onError(errorHandler.handleError(t));
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

    protected Context getContext() {
        return mContext;
    }

}
