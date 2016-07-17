package com.peekaboo.presentation.presenters;

import com.peekaboo.presentation.views.ISignUpView;

/**
 * Created by sebastian on 28.06.16.
 */
public interface ISignUpPresenter extends IPresenter<ISignUpView> {
    void onSignUpButtonClick(String login, String email, String password, String passwordConfirm);
    void onCodeConfirmButtonClick(String key);
}
