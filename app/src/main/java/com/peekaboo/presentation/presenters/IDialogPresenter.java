package com.peekaboo.presentation.presenters;

import com.peekaboo.presentation.views.IDialogsView;

/**
 * Created by st1ch on 27.09.2016.
 */

public interface IDialogPresenter extends IPresenter<IDialogsView>{

    void onCreate();
    void onPause();
    void onDestroy();

    void loadDialogList();
}
