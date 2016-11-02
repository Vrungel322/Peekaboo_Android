package com.peekaboo.presentation.presenters;

import android.net.Uri;

import com.peekaboo.presentation.views.IView;

/**
 * Created by Nikita on 02.11.2016.
 */
public interface ISettingsPresenter <V extends IView> extends IPresenter<V> {
    void updateAvatarInSettings(Uri avatarUri);
}
