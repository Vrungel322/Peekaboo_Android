package com.peekaboo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.peekaboo.R;
import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.presentation.activities.DetailsActivity;
import com.peekaboo.presentation.activities.LogInActivity;
import com.peekaboo.presentation.activities.MainActivity;
import com.peekaboo.presentation.activities.MapActivity;
import com.peekaboo.presentation.activities.SignUpActivity;
import com.peekaboo.presentation.animation.DepthAnimation;
import com.peekaboo.presentation.fragments.CallsFragment;
import com.peekaboo.presentation.fragments.ContactsFragment;
import com.peekaboo.presentation.fragments.DialogsFragment;
import com.peekaboo.presentation.fragments.ProfileFragment;
import com.peekaboo.presentation.fragments.SettingsFragment;
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

    public void startMainActivity(Context mActivityContext, @Nullable String intentAction) {
        Intent intent = new Intent(mActivityContext, MainActivity.class);
        if (intentAction != null) {
            intent.setAction(intentAction);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }
        mActivityContext.startActivity(intent);
    }

    public void startChat(AppCompatActivity activity, Contact companion) {
        Intent intent = new Intent(activity, DetailsActivity.class);
        intent.setAction(DetailsActivity.ACTION_CHAT);
        intent.putExtra(DetailsActivity.EXTRA, companion);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }

    public void startSmsChat(AppCompatActivity activity, PhoneContactPOJO contact) {
        Intent intent = new Intent(activity, DetailsActivity.class);
        intent.setAction(DetailsActivity.ACTION_SMS_CHAT);
        intent.putExtra(DetailsActivity.EXTRA, contact);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
//        FragmentTransaction replace = activity.getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.toolbar_fragment_container, SmsChatFragment.newInstance(companion));
//        if (addToBackStack) {
//            replace.addToBackStack(null);
//        }
//        replace.commit();
    }

    public void startFragment(AppCompatActivity appCompatActivity, @NonNull String tag) {
        FragmentManager fm = appCompatActivity.getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null || !tag.equals(fragment.getTag())) {
            fragment = null;
            switch (tag) {
                case Constants.FRAGMENT_TAGS.DIALOGS_FRAGMENT:
                    fragment = DialogsFragment.newInstance();
                    break;
                case Constants.FRAGMENT_TAGS.CONTACTS_FRAGMENT:
                    fragment = ContactsFragment.newInstance();
                    break;
                case Constants.FRAGMENT_TAGS.CALLS_FRAGMENT:
                    fragment = new CallsFragment();
                    break;
                case Constants.FRAGMENT_TAGS.PROFILE_FRAGMENT:
                    fragment = new ProfileFragment();
                    break;
                case Constants.FRAGMENT_TAGS.SETTINGS_FRAGMENT:
                    fragment = new SettingsFragment();
            }
            if (fragment != null) {
                fm.beginTransaction()
                        .replace(R.id.fragmentContainer, fragment, tag)
                        .commit();
            }
        }
    }

    public void startMapActivity(Activity activity, int requestCodeGps) {
        Intent mapIntent = new Intent(activity, MapActivity.class);
        activity.startActivityForResult(mapIntent, requestCodeGps);
        Log.wtf("NULL : ", "sendim gpsimg in fragment");
    }
}
