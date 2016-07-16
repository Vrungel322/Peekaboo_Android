package com.peekaboo.presentation.di;

import com.peekaboo.domain.User;
import com.peekaboo.presentation.activities.LogInActivity;
import com.peekaboo.presentation.activities.MainActivity;
import com.peekaboo.presentation.activities.SplashActivity;
import com.peekaboo.presentation.activities.SignUpActivity;
import com.peekaboo.presentation.fragments.ConfirmSignUpDialog;
import com.peekaboo.presentation.services.INotifier;
import com.peekaboo.presentation.services.NotificationService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(SplashActivity splashActivity);
    void inject(LogInActivity logInActivity);
    void inject(SignUpActivity signUpActivity);
    void inject (ConfirmSignUpDialog confirmSignUpDialog);
    void inject(NotificationService notificationService);
    void inject(MainActivity mainActivity);
}
