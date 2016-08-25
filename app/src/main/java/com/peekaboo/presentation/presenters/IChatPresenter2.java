package com.peekaboo.presentation.presenters;

import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.presentation.views.IView;

/**
 * Created by sebastian on 24.08.16.
 */
public interface IChatPresenter2<V extends IView> extends IPresenter<V>{

    void onResume(boolean isFirstLaunch, String receiver);

    void onPause();

    void onSendTextButtonPress(String receiver, String text);

    void onUserMessageRead(PMessage message);
}
