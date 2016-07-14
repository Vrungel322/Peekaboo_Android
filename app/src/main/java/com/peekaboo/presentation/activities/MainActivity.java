package com.peekaboo.presentation.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;

import com.peekaboo.R;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.di.ApplicationComponent;
import com.peekaboo.presentation.fragments.ContactsFragment;
import com.peekaboo.presentation.fragments.DialogsFragment;
import com.peekaboo.presentation.fragments.ProfileFragment;
import com.peekaboo.presentation.fragments.SearchFragment;
import com.peekaboo.presentation.fragments.SettingsFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @Inject
    DialogsFragment dialogsFragment;
    @Inject
    ContactsFragment contactsFragment;
    @Inject
    ProfileFragment profileFragment;
    @Inject
    SearchFragment searchFragment;
    @Inject
    SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ApplicationComponent component = PeekabooApplication.getApp(this).getComponent();
        component.inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    @OnClick(R.id.bDialogs)
    public void bDialogsClick(){
        changeFragment(dialogsFragment, "dialogsFragment");
    }

    @OnClick(R.id.bContacts)
    public void bContactsClick(){
        changeFragment(contactsFragment, "contactsFragment");
    }

    @OnClick(R.id.bProfile)
    public void bProfileClick(){
        changeFragment(profileFragment, "profileFragment");
    }


    @OnClick(R.id.bSearch)
    public void bSearchClick(){
        changeFragment(searchFragment, "searchFragment");
    }

    @OnClick(R.id.bSettings)
    public void bSettingsClick(){
        changeFragment(settingsFragment, "settingsFragment");
    }

    private void changeFragment(Fragment fragment, String tag) {
        getFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment, tag)
                .commit();
        drawer.closeDrawer(Gravity.LEFT);
    }

}
