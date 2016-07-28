package com.peekaboo.presentation.presenters;

import com.peekaboo.data.repositories.database.PMessage;
import com.peekaboo.data.repositories.database.PMessageAbs;
import com.peekaboo.presentation.views.IChatView;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by st1ch on 28.07.2016.
 */
public interface IChatPresenter extends IPresenter<IChatView> {
    void createTable(String tableName);
    void insertMessageToTable(String tableName, PMessage message);
    void dropTableAndCreate(String tableName);
    Subscription getAllMessages(String tableName, Action1 adapter);
    int deleteMessageByPackageId(String tableName, PMessageAbs message);
    void copyMessageText(PMessageAbs message);
    void convertTextToSpeech(PMessageAbs message);
    void onPause();
    void onResume();
}
