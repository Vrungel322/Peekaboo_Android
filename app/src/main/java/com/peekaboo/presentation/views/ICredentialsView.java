package com.peekaboo.presentation.views;

/**
 * Created by Nikita on 27.06.2016.
 */

public interface ICredentialsView extends IProgressView {
    enum InputFieldError {USERNAME, LOGIN, PASSWORD, PASSWORD_CONFIRM}

    void navigateToProfile();
    void showInputError(InputFieldError error);
}
