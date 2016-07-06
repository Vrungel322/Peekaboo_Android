package com.peekaboo.presentation.presenters;

/**
 * Created by sebastian on 28.06.16.
 */
public interface ISignUpPresenter extends IPresenter<ISingUpView> {
    void onSignUpButtonClick(String login, String email, String password, String passwordConfirm);
}
