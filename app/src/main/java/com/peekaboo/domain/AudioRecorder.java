package com.peekaboo.domain;

import android.util.Log;

import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;

import javax.inject.Inject;

import rx.Observable;
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
        return Observable.just(record)
                .subscribeOn(Schedulers.computation())
                .map(record -> {
                    Log.e("record", "start map " + Thread.currentThread().hashCode());
                    record.startRecording();
                    return record;
                });
    }

    public Observable<Record> stopRecording() {
        isRecording = false;
        return Observable.just(record)
                .subscribeOn(Schedulers.computation())
                .map(record -> {
                    Log.e("record", "stop map " + Thread.currentThread().hashCode());
                    record.stopRecording();
                    return record;
                })
                .observeOn(observeOn.getScheduler());
    }

    public boolean isRecording() {
        return isRecording;
    }
}
