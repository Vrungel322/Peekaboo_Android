package com.peekaboo.presentation.presenters;

import com.peekaboo.presentation.views.ISmsChatView;

/**
 * Created by st1ch on 10.10.2016.
 */

public interface ISmsChatPresenter extends IPresenter<ISmsChatView> {
    void onCreate(String receiverPhone);
    void onResume();
    void onPause();
    void onDestroy();
    void sendMessage(String message);
    void getAllContactsMessageList();
}
