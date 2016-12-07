package com.peekaboo.presentation.fragments;

import com.peekaboo.domain.AccountUser;
import com.peekaboo.presentation.views.IProgressView;

/**
 * Created by Nikita on 02.11.2016.
 */
public interface ISettingsView extends IProgressView {
    void renderSettings(AccountUser accountUser);

    void updateAvatarViewInSettings(String result);
     void updateAccountUserFromSettings();
}
