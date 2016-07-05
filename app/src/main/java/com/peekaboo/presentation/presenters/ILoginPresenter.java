package com.peekaboo.presentation.presenters;

import com.peekaboo.presentation.views.ICredentialsView;

/**
 * Created by sebastian on 28.06.16.
 */
public interface ILoginPresenter extends IPresenter<ICredentialsView> {
    void onSignInButtonClick(String login, String password);
    void onVkButtonClick();
}
