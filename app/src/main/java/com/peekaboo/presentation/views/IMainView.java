package com.peekaboo.presentation.views;

import com.peekaboo.domain.Dialog;

import java.util.List;

/**
 * Created by Nikita on 28.09.2016.
 */
public interface  IMainView extends IView, IProgressView {
    void hotFriendToShow(List<Dialog> hotDialogs);
}
