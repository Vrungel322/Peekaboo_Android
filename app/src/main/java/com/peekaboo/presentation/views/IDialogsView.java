package com.peekaboo.presentation.views;

import com.peekaboo.domain.Dialog;

import java.util.List;

/**
 * Created by st1ch on 27.09.2016.
 */

public interface IDialogsView extends IProgressView {
    void showDialogsList(List<Dialog> dialogs);
}
