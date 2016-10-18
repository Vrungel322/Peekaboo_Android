package com.peekaboo.presentation.views;

/**
 * Created by st1ch on 28.07.2016.
 */
public interface IChatView extends IView {

    void clearTextField();
    String getMessageText();
    void updateAudioProgress(int position, long totalDuration, long currentDuration, int progress);
    void switchPlayButtonImage(int position, boolean toPlay);
//    void
}
