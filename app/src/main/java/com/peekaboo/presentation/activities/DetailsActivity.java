package com.peekaboo.presentation.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.peekaboo.R;
import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.fragments.ChatFragment;
import com.peekaboo.presentation.utils.ActivityUtils;

/**
 * Created by arkadii on 11/28/16.
 */

public class DetailsActivity extends DrawerActivity {

    public static final String ACTION_CHAT = "action_chat";
    public static final String EXTRA = "extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        handleIntent(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case ACTION_CHAT:
                    Contact contact = (Contact) intent.getParcelableExtra(EXTRA);
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
                    if (fragment == null || !(fragment instanceof ChatFragment) ||
                            !((ChatFragment) fragment).getCompanionId().equals(contact.contactId())) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainer, ChatFragment.newInstance(contact))
                                .commit();
                    }
            }
        }
    }

    @Override
    protected void prepareActionBar(Toolbar toolbar, DrawerLayout drawer) {
        toolbar.setNavigationIcon(R.drawable.back_arrow1);
        toolbar.setNavigationOnClickListener(v -> {
            ActivityUtils.hideKeyboard(this);
            finish();
        });
    }

    @Override
    protected void inject() {
        PeekabooApplication.getApp(this).getComponent().inject(this);
    }

    @Override
    protected void onDrawerClosed() {

    }

    @Override
    protected void handleDrawerClick(int id) {

    }


}
