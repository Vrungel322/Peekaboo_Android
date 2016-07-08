package com.peekaboo.presentation.presenters;

import com.peekaboo.presentation.views.ISingUpView;

/**
 * Created by sebastian on 28.06.16.
 */
public interface ISignUpPresenter extends IPresenter<ISingUpView> {
    void onSignUpButtonClick(String username, String login, String password, String passwordConfirm);
    void onCodeConfirmButtonClick(String key);
}
