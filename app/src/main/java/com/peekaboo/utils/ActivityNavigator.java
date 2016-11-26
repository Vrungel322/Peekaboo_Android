package com.peekaboo.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.peekaboo.R;
import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.presentation.activities.LogInActivity;
import com.peekaboo.presentation.activities.MainActivity;
import com.peekaboo.presentation.activities.SignUpActivity;
import com.peekaboo.presentation.animation.DepthAnimation;
import com.peekaboo.presentation.fragments.ChatFragment;
import com.peekaboo.presentation.fragments.DialogsFragment;
import com.peekaboo.presentation.pojo.PhoneContactPOJO;

import javax.inject.Inject;

/**
 * Created by Nikita on 28.06.2016.
 */

public class ActivityNavigator {


    @Inject
    public ActivityNavigator() {
    }

    public void startLogInActivity(Context mActivityContext, boolean newTask) {
        Intent intent = new Intent(mActivityContext, LogInActivity.class);
        if (newTask) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
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

    public void startChatFragment(AppCompatActivity activity, Contact companion, boolean addToBackStack) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();

        Fragment chatFragment = fragmentManager.findFragmentByTag(Constants.FRAGMENT_TAGS.CHAT_FRAGMENT_TAG);
        if (chatFragment != null) {
            fragmentManager.popBackStack();
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (addToBackStack) {
            transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_in, R.anim.pop_out);
        }
        transaction.replace(R.id.fragmentContainer, ChatFragment.newInstance(companion), Constants.FRAGMENT_TAGS.CHAT_FRAGMENT_TAG);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
        fragmentManager.executePendingTransactions();
    }

    public void startSmsChatFragment(AppCompatActivity activity, PhoneContactPOJO companion, boolean addToBackStack) {
//        FragmentTransaction replace = activity.getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.toolbar_fragment_container, SmsChatFragment.newInstance(companion));
//        if (addToBackStack) {
//            replace.addToBackStack(null);
//        }
//        replace.commit();
    }

    public void startDialogFragment(AppCompatActivity appCompatActivity) {
        FragmentManager fm = appCompatActivity.getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        fm.beginTransaction()
                .replace(R.id.fragmentContainer, DialogsFragment.newInstance(), com.peekaboo.utils.Constants.FRAGMENT_TAGS.DIALOGS_FRAGMENT)
                .commit();
    }
}
