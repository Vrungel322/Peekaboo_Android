package com.peekaboo.presentation.views;

import com.peekaboo.domain.Sms;

import java.util.List;

/**
 * Created by st1ch on 10.10.2016.
 */

public interface ISmsChatView extends IProgressView {

    void showMessages(List<Sms> messages);
    void appendMessage(Sms message);
    void clearTextField();

}
