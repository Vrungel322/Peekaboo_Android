package com.peekaboo.domain.usecase;

import android.support.annotation.Nullable;
import android.util.Log;

import com.peekaboo.domain.Pair;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by sebastian on 14.09.16.
 */
public abstract class QueueUseCase<K, V> {
    private final ObserveOn observeOn;
    private SubscribeOn subscribeOn;
    private Observable<Pair<K, V>> observable;
    private BlockingQueue<K> queue = new LinkedBlockingQueue<>();

    public QueueUseCase(ObserveOn observeOn) {
        this.observeOn = observeOn;
    }

    public void execute(K key, Subscriber<Pair<K, V>> subscriber) {
        queue.add(key);
        if (observable == null) {
            subscribeOn = () -> Schedulers.from(Executors.newSingleThreadExecutor());

            observable = getNewObservable();
            observable.subscribe(subscriber);
        }
    }

    private Observable<Pair<K, V>> getNewObservable() {
        return Observable.create(new Observable.OnSubscribe<Pair<K, V>>() {
            @Override
            public void call(Subscriber<? super Pair<K, V>> subscriber) {
                while (!queue.isEmpty()) {
                    try {
                        K take = queue.take();
                        V value = getValue(take);
                        if (value != null) {
                            subscriber.onNext(new Pair<>(take, value));
                        }
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                }
                subscriber.onCompleted();
            }
        })
                .subscribeOn(subscribeOn.getScheduler())
                .observeOn(observeOn.getScheduler())
                .doOnError((t) -> {
                    observable = null;
                    subscribeOn = null;
                })
                .doOnCompleted(() -> {
                    observable = null;
                    subscribeOn = null;
                });
    }

    @Nullable
    protected abstract V getValue(K take) throws IOException;
}
