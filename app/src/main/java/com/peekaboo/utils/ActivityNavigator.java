package com.peekaboo.utils;

import android.content.Context;
import android.content.Intent;

import com.peekaboo.presentation.activities.ChatActivity2;
import com.peekaboo.presentation.activities.LogInActivity;
import com.peekaboo.presentation.activities.MainActivity;
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
        Intent intent = new Intent(mActivityContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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

    public void startMainActivity(Context mActivityContext) {
        Intent intent = new Intent(mActivityContext, MainActivity.class);
        mActivityContext.startActivity(intent);
    }

    public void startChatActivity(Context context, String companionId) {
        Intent intent = new Intent(context, ChatActivity2.class);
        intent.putExtra(ChatActivity2.COMPANION_ID, companionId);
        context.startActivity(intent);
    }
}
