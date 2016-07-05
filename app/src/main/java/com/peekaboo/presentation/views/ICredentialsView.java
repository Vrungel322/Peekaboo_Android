package com.peekaboo.presentation.views;

/**
 * Created by Nikita on 27.06.2016.
 */

public interface ICredentialsView extends IProgressView {
    enum InputFieldError {LOGIN, PASSWORD, PASSWORD_CONFIRM, EMAIL};
    void navigateToProfile();
    void showInputError(InputFieldError error);
}
