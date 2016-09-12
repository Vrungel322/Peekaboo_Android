package com.peekaboo.domain;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by st1ch on 28.07.2016.
 */
public class AudioRecorder {

    private Record record;
    private OnStartRecordingListener onStartRecordingListener;
    private OnStopRecordingListener onStopRecordingListener;

    public AudioRecorder(Record record) {
        this.record = record;
    }

    public Observable<Record> startRecording() {
        onStartRecordingListener.onStartRecording();
        return Observable.just(record)
                .subscribeOn(Schedulers.computation())
                .map(record -> {
                    record.startRecording();
                    return record;
                });
    }

    public Observable<Record> stopRecording() {
        return Observable.just(record)
                .subscribeOn(Schedulers.computation())
                .map(record -> {
                    onStopRecordingListener.onStopRecording();
                    record.stopRecording();
                    return record;
                });
    }

    public void setOnStartRecordingListener(OnStartRecordingListener listener){
        onStartRecordingListener = listener;
    }

    public void setOnStopRecordingListener(OnStopRecordingListener listener){
        onStopRecordingListener = listener;
    }

    public interface OnStartRecordingListener {
        void onStartRecording();
    }

    public interface OnStopRecordingListener {
        void onStopRecording();
    }

}
