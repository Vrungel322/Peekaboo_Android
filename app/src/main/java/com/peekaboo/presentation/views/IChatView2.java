package com.peekaboo.presentation.views;

import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.data.repositories.database.messages.PMessageAbs;

import java.util.List;

/**
 * Created by sebastian on 24.08.16.
 */
public interface IChatView2 extends IView {
    void clearTextField();

    String getMessageText();

    void showMessages(List<PMessage> messages);
    void appendMessages(List<PMessage> messages);
    void updateMessage(PMessage message);
    String getCompanionId();
}
