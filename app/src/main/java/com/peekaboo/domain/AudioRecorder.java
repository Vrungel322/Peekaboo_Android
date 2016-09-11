package com.peekaboo.domain;

import android.util.Log;

import rx.Observable;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by st1ch on 28.07.2016.
 */
public class AudioRecorder implements IAudioRecorder {

    private Record record;
    private boolean isRecording;
    public void setRecord(Record record) {
        this.record = record;
    }

    @Override
    public Observable<Record> startRecording() {
        isRecording = true;
        return Observable.just(record)
                .subscribeOn(Schedulers.computation())
                .map(record -> {
                    Log.e("recorder", "map start");
                    record.startRecording();
                    return record;
                });
    }

    @Override
    public Observable<Record> stopRecording() {
        isRecording = false;
        return Observable.just(record)
                .subscribeOn(Schedulers.computation())
                .map(record -> {
                    Log.e("recorder", "map stop");
                    record.stopRecording();
                    return record;
                });
    }

    @Override
    public boolean isRecording() {
        return isRecording;
    }
}
