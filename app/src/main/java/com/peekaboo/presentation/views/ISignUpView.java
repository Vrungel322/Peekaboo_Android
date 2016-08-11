package com.peekaboo.presentation.views;

import com.peekaboo.presentation.views.ICredentialsView;

/**
 * Created by Nikita on 06.07.2016.
 */
public interface ISignUpView extends ICredentialsView {
    void showConfirmDialog();

    void dismissConfirmDialog();
}
