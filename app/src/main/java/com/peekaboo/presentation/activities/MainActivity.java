package com.peekaboo.presentation.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;

import com.peekaboo.R;
import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.fragments.CallsFragment;
import com.peekaboo.presentation.fragments.ContactsFragment;
import com.peekaboo.presentation.fragments.DialogsFragment;
import com.peekaboo.presentation.fragments.ProfileFragment;
import com.peekaboo.presentation.fragments.SettingsFragment;
import com.peekaboo.presentation.services.MessageNotificator;
import com.peekaboo.utils.Constants;

public class MainActivity extends DrawerActivity {
    Fragment fragment;
    String fragmentTag;

    @Override
    protected void onDrawerClosed() {
        if (fragment != null) {
            changeFragment(fragment, fragmentTag);
            fragment = null;
            fragmentTag = null;
        }
    }


    @Override
    protected void handleDrawerClick(int id) {
        if (id != R.id.llExit) {
            FragmentManager fm = getSupportFragmentManager();
            for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
        }
        switch (id) {
            case R.id.llDialogs:
                fragment = DialogsFragment.newInstance();
                fragmentTag = Constants.FRAGMENT_TAGS.DIALOGS_FRAGMENT;
                break;
            case R.id.llCalls:
                fragment = new CallsFragment();
                fragmentTag = Constants.FRAGMENT_TAGS.CALLS_FRAGMENT;
                break;
            case R.id.llContacts:
                fragment = ContactsFragment.newInstance();
                fragmentTag = Constants.FRAGMENT_TAGS.CONTACTS_FRAGMENT;
                break;
            case R.id.llProfile:
                fragment = new ProfileFragment();
                fragmentTag = Constants.FRAGMENT_TAGS.PROFILE_FRAGMENT;
                break;
            case R.id.llSettings:
                fragment = new SettingsFragment();
                fragmentTag = Constants.FRAGMENT_TAGS.SETTINGS_FRAGMENT;
                break;
        }
    }

    @Override
    protected void prepareActionBar(Toolbar toolbar, DrawerLayout drawer) {
        toolbar.setNavigationIcon(R.drawable.burger);
        toolbar.setNavigationOnClickListener(v -> {
            if (!drawer.isDrawerOpen(Gravity.LEFT)) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportFragmentManager().findFragmentById(R.id.fragmentContainer) == null) {
            Intent intent = getIntent();
            handleNotificationIntent(intent);

            if (!MainActivity.ACTION.SHOW_DIALOGS.equals(intent.getAction())) {
                changeFragment(ContactsFragment.newInstance(), Constants.FRAGMENT_TAGS.CONTACTS_FRAGMENT);
                selectionMode(R.id.llContacts);
            }

        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleNotificationIntent(intent);
    }

    private void handleNotificationIntent(Intent intent) {
        String action = intent.getAction();
        if (action != null) {

            switch (action) {
                case ACTION.SHOW_CONTACTS:
                    changeFragment(ContactsFragment.newInstance(), Constants.FRAGMENT_TAGS.CONTACTS_FRAGMENT);
                    selectionMode(R.id.llContacts);
                    break;
                case ACTION.SHOW_CALLS:
                    changeFragment(new CallsFragment(), Constants.FRAGMENT_TAGS.CALLS_FRAGMENT);
                    selectionMode(R.id.llCalls);
                    break;
                case ACTION.SHOW_DIALOGS: {
                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(MessageNotificator.NOTIFICATION_ID);
                    navigator.startDialogFragment(this);
                }
                selectionMode(R.id.llDialogs);
                break;
                case ACTION.SHOW_SETTINGS:
                    changeFragment(new SettingsFragment(), Constants.FRAGMENT_TAGS.SETTINGS_FRAGMENT);
                    selectionMode(R.id.llSettings);
                    break;
                case ACTION.SHOW_PROFILE:
                    changeFragment(new ProfileFragment(), Constants.FRAGMENT_TAGS.PROFILE_FRAGMENT);
                    selectionMode(R.id.llProfile);
                    break;
                case ACTION.SHOW_CHAT: {
                    Contact contact = intent.getParcelableExtra(MainActivity.ACTION.EXTRA.CONTACT_EXTRA);
                    if (contact != null) {
                        NotificationManager notificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancel(MessageNotificator.NOTIFICATION_ID);
                        navigator.startChat(this, contact);
                    }
                }
                break;
            }
        }
    }

    public interface ACTION {
        String SHOW_DIALOGS = "action.show_dialogs";
        String SHOW_CHAT = "action.show_chat";
        String SHOW_CONTACTS = "action.show_contacts";
        String SHOW_CALLS = "action.show_calls";
        String SHOW_PROFILE = "action.show_profile";
        String SHOW_SETTINGS = "action.show_settings";

        interface EXTRA {
            String CONTACT_EXTRA = "contact_extra";
        }

    }

    @Override
    protected void inject() {
        PeekabooApplication.getApp(this).getComponent().inject(this);
    }


}
