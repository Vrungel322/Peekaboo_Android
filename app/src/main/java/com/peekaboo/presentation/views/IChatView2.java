package com.peekaboo.presentation.views;

import com.peekaboo.data.repositories.database.messages.PMessageAbs;

import java.util.List;

/**
 * Created by sebastian on 24.08.16.
 */
public interface IChatView2 extends IView {
    void showMessages(List<PMessageAbs> messages);
    void appendMessages(List<PMessageAbs> messages);
    void updateMessage(PMessageAbs message);
    String getCompanionId();
}
