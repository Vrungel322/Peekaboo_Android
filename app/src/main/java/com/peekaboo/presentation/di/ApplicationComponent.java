package com.peekaboo.presentation.di;

import com.peekaboo.presentation.activities.LogInActivity;
import com.peekaboo.presentation.activities.MainActivity;
import com.peekaboo.presentation.activities.SignUpActivity;
import com.peekaboo.presentation.fragments.ConfirmSignUpDialog;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(MainActivity mainActivity);
    void inject(LogInActivity logInActivity);
    void inject(SignUpActivity signUpActivity);
    void inject (ConfirmSignUpDialog confirmSignUpDialog);
}
