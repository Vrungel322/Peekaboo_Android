package com.peekaboo.presentation.presenters;

import com.peekaboo.presentation.views.ILoginView;

/**
 * Created by sebastian on 28.06.16.
 */
public interface ILoginPresenter extends IPresenter<ILoginView> {
    void onSignInButtonClick(String login, String password);
}
