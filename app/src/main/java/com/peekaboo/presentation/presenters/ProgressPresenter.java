package com.peekaboo.presentation.presenters;

import android.content.Context;

import com.peekaboo.domain.UserMessageMapper;
import com.peekaboo.domain.subscribers.BaseProgressSubscriber;
import com.peekaboo.presentation.views.IProgressView;

/**
 * Created by sebastian on 28.06.16.
 */
public abstract class ProgressPresenter<V extends IProgressView> extends BasePresenter<V> implements
        BaseProgressSubscriber.ProgressSubscriberListener {

    private UserMessageMapper errorHandler;

    public ProgressPresenter(UserMessageMapper errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Override
    public void onError(Throwable t) {
        V view = getView();
        if (view != null) {
            view.hideProgress();
            view.showToastMessage(errorHandler.handleError(t));
        }
    }

    @Override
    public void onCompleted() {
        V view = getView();
        if (view != null) {
            view.hideProgress();
        }
    }

    @Override
    public void onStartLoading() {
        V view = getView();
        if (view != null) {
            view.showProgress();
        }
    }

    public void showMessage(int stringId) {
        V view = getView();
        if (view != null) {
            view.showToastMessage(errorHandler.getMessageFromResource(stringId));
        }
    }

    public UserMessageMapper getErrorHandler() {
        return errorHandler;
    }
}
