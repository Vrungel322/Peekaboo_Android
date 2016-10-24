package com.peekaboo.presentation.views;

import com.peekaboo.domain.SmsDialog;

import java.util.List;

/**
 * Created by st1ch on 19.10.2016.
 */

public interface ISmsDialogsView extends IProgressView {
    void showDialogsList(List<SmsDialog> dialogs);
    void updateList();
}
