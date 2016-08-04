package com.peekaboo.presentation.services;

import rx.Observable;
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
            record.startRecording();
            return record;
        });
    }

    @Override
    public Observable<Record> stopRecording() {
        return Observable.just(record)
                .subscribeOn(Schedulers.computation())
                .map(record -> {
                    record.stopRecording();
                    return record;
                });
    }

}
