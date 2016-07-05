package com.peekaboo.presentation.presenters;

import com.peekaboo.presentation.views.ICredentialsView;

/**
 * Created by sebastian on 28.06.16.
 */
public interface ISignUpPresenter extends IPresenter<ICredentialsView> {
    void onSignUpButtonClick(String login, String email, String password, String passwordConfirm);
}
