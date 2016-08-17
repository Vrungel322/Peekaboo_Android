package com.peekaboo.presentation.presenters;

import android.content.Context;

import com.peekaboo.domain.ErrorHandler;
import com.peekaboo.domain.subscribers.BaseProgressSubscriber;
import com.peekaboo.presentation.views.IProgressView;

/**
 * Created by sebastian on 28.06.16.
 */
public abstract class ProgressPresenter<V extends IProgressView> extends BasePresenter<V> implements
        BaseProgressSubscriber.ProgressSubscriberListener {

    private Context mContext;
    private ErrorHandler errorHandler;

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

    }

    protected Context getContext() {
        return mContext;
    }

}
