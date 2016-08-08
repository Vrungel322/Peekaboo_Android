package com.peekaboo.presentation.services;

import rx.Observable;

/**
 * Created by st1ch on 28.07.2016.
 */
public interface IAudioRecorder {
    Observable<Record> startRecording();
    Observable<Record> stopRecording();
}
