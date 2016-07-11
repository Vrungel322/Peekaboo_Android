package com.peekaboo.utils;

import android.content.Context;
import android.content.Intent;

import com.peekaboo.presentation.activities.LogInActivity;
import com.peekaboo.presentation.activities.DialogActivity;
import com.peekaboo.presentation.activities.SignUpActivity;
import com.peekaboo.presentation.animation.DepthAnimation;

import javax.inject.Inject;

/**
 * Created by Nikita on 28.06.2016.
 */

public class ActivityNavigator {

    @Inject
    public ActivityNavigator() {
    }

    public void startLogInActivity(Context mActivityContext){
        Intent intent = new Intent(mActivityContext, LogInActivity.class);
        mActivityContext.startActivity(intent);
    }

    public void startProfileActivity(Context mActivityContext){
        Intent intent = new Intent(mActivityContext, DialogActivity.class);
        mActivityContext.startActivity(intent);
    }

    public void startIntroScreen(Context mActivityContext){
        Intent intent = new Intent(mActivityContext, DepthAnimation.class);
        mActivityContext.startActivity(intent);
    }

    public void startSignUpActivity(Context mActivityContext){
        Intent intent = new Intent(mActivityContext, SignUpActivity.class);
        mActivityContext.startActivity(intent);
    }

}
