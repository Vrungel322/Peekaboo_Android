package com.peekaboo.presentation.presenters;

/**
 * Created by st1ch on 10.10.2016.
 */

public interface ISmsDialogPresenter {
    void onCreate();
    void onResume(boolean isFirstLaunch);
    void onPause();
    void onDestroy();
    void loadDialogsList();

    void loadDialogs();
}
