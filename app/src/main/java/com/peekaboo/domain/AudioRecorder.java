package com.peekaboo.domain;

import android.util.Log;

import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by st1ch on 28.07.2016.
 */
public class AudioRecorder {

    private Record record;
    private boolean isRecording;
    private SubscribeOn subscribeOn;
    private ObserveOn observeOn;

    @Inject
    public AudioRecorder(SubscribeOn subscribeOn, ObserveOn observeOn) {
        this.subscribeOn = subscribeOn;
        this.observeOn = observeOn;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public Observable<Record> startRecording() {
        isRecording = true;
        return Observable.create(new Observable.OnSubscribe<Record>() {
            @Override
            public void call(Subscriber<? super Record> subscriber) {
                try {
                    record.startRecording();
                    subscriber.onNext(record);
                    subscriber.onCompleted();
                } catch (RuntimeException re) {
                    subscriber.onError(re);
                }
            }
        }).subscribeOn(Schedulers.computation()).observeOn(observeOn.getScheduler());
    }

    public Observable<Record> stopRecording() {
        isRecording = false;
        return Observable.create(new Observable.OnSubscribe<Record>() {
            @Override
            public void call(Subscriber<? super Record> subscriber) {
                try {
                    record.stopRecording();
                    subscriber.onNext(record);
                    subscriber.onCompleted();
                } catch (RuntimeException re) {
                    subscriber.onError(re);
                }
            }
        }).subscribeOn(Schedulers.computation()).observeOn(observeOn.getScheduler());
    }

    public boolean isRecording() {
        return isRecording;
    }
}
