package com.peekaboo.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.peekaboo.R;
import com.peekaboo.data.*;
import com.peekaboo.data.Constants;
import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.presentation.activities.ChatFragment;
import com.peekaboo.presentation.activities.LogInActivity;
import com.peekaboo.presentation.activities.MainActivity;
import com.peekaboo.presentation.activities.SignUpActivity;
import com.peekaboo.presentation.animation.DepthAnimation;
import com.peekaboo.presentation.fragments.DialogsFragment;

import javax.inject.Inject;

/**
 * Created by Nikita on 28.06.2016.
 */

public class ActivityNavigator {

    @Inject
    public ActivityNavigator() {
    }

    public void startLogInActivity(Context mActivityContext) {
        Intent intent = new Intent(mActivityContext, LogInActivity.class);
        mActivityContext.startActivity(intent);
    }

    public void startProfileActivity(Context mActivityContext) {
        Intent intent = new Intent(mActivityContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivityContext.startActivity(intent);
    }

    public void startIntroScreen(Context mActivityContext) {
        Intent intent = new Intent(mActivityContext, DepthAnimation.class);
        mActivityContext.startActivity(intent);
    }

    public void startSignUpActivity(Context mActivityContext) {
        Intent intent = new Intent(mActivityContext, SignUpActivity.class);
        mActivityContext.startActivity(intent);
    }

    public void startMainActivity(Context mActivityContext) {
        Intent intent = new Intent(mActivityContext, MainActivity.class);
        mActivityContext.startActivity(intent);
    }

    public void startChatActivity(AppCompatActivity activity, Contact companion, boolean addToBackStack) {
        Log.e("notif", "start chat " + companion);
        FragmentTransaction replace = activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, ChatFragment.newInstance(companion));
        if (addToBackStack) {
            replace.addToBackStack(null);
        }
        replace.commit();
    }

    public void startDialogFragment(AppCompatActivity appCompatActivity) {
        Log.e("notif", "start dialog");
        FragmentManager fm = appCompatActivity.getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        DialogsFragment fragment = new DialogsFragment();
        fm.beginTransaction()
                .replace(R.id.fragmentContainer, fragment, com.peekaboo.utils.Constants.FRAGMENT_TAGS.DIALOGS_FRAGMENT)
                .commit();
    }
}
