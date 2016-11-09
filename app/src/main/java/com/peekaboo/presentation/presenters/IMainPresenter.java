package com.peekaboo.presentation.presenters;

import android.net.Uri;

import com.peekaboo.presentation.views.IView;

/**
 * Created by Nikita on 28.09.2016.
 */
public interface IMainPresenter <V extends IView> extends IPresenter<V> {
    void fillHotAdapter();
    void updateAvatar(String avatarPath);
}
