package com.peekaboo.domain;

import android.util.Log;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by st1ch on 28.07.2016.
 */
public class AudioRecorder implements IAudioRecorder {

    private Record record;

    public AudioRecorder(Record record) {
        this.record = record;
    }

    @Override
    public Observable<Record> startRecording() {
        return Observable.just(record)
                .subscribeOn(Schedulers.computation())
                .map(record -> {
                    Log.e("record", "start map");
                    record.startRecording();
                    return record;
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Record> stopRecording() {
        return Observable.just(record)
                .subscribeOn(Schedulers.computation())
                .map(record -> {
                    Log.e("record", "stop map");
                    record.stopRecording();
                    return record;
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

}
