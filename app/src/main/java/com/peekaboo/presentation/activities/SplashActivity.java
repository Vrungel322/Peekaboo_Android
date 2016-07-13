package com.peekaboo.presentation.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.peekaboo.R;
import com.peekaboo.domain.User;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.di.ApplicationComponent;
import com.peekaboo.presentation.presenters.SplashActivityPresenter;
import com.peekaboo.utils.ActivityNavigator;

import javax.inject.Inject;

public class SplashActivity extends AppCompatActivity {
    //
    @Inject
    ActivityNavigator mNavigator;
    @Inject
    SplashActivityPresenter splashActivityPresenter;
    @Inject
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ApplicationComponent component = PeekabooApplication.getApp(this).getComponent();
        component.inject(this);

        if (splashActivityPresenter.isFirstLaunch()) {
            splashActivityPresenter.setSecondLaunch();
            mNavigator.startIntroScreen(this);
        } else if (user.isAuthorized()) {
            mNavigator.startMainActivity(this);
        } else {
            mNavigator.startLogInActivity(this);
        }
        finish();
    }
}
