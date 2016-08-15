package com.peekaboo.domain.subscribers;

import android.util.Log;

import rx.Subscriber;

public abstract class BaseUseCaseSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        Log.e("exception", String.valueOf(e));
    }

    @Override
    public void onNext(T t) {}
}
