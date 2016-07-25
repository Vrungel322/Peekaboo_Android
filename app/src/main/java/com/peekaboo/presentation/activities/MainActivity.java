package com.peekaboo.presentation.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.Toast;

import com.peekaboo.R;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.di.ApplicationComponent;
import com.peekaboo.presentation.fragments.CallsFragment;
import com.peekaboo.presentation.fragments.ContactsFragment;
import com.peekaboo.presentation.fragments.DialogsFragment;
import com.peekaboo.presentation.fragments.ProfileFragment;
import com.peekaboo.presentation.fragments.SettingsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

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
//        changeFragment(new ServiceTestFragment(), null);
    }


    @OnClick(R.id.lvDialogs)
    public void lvDialogsClick(){
        changeFragment(new DialogsFragment(), "dialogsFragment");
    }

    @OnClick(R.id.lvCalls)
    public void lvCallsClick(){
        changeFragment(new CallsFragment(), "callsFragment");
    }
    @OnClick(R.id.lvContacts)
    public void lvContactsClick(){
        changeFragment(new ContactsFragment(), "contactsFragment");
    }

    @OnClick(R.id.lvProfile)
    public void lvProfileClick(){
        changeFragment(new ProfileFragment(), "profileFragment");
    }

    @OnClick(R.id.lvSettings)
    public void lvSettingsClick(){
        changeFragment(new SettingsFragment(), "settingsFragment");
    }

    @OnClick(R.id.lvExit)
    public void lvExitClick(){
        Toast.makeText(getApplicationContext(), "Log out", Toast.LENGTH_LONG).show();
    }

    private void changeFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment, tag)
                .commit();
        drawer.closeDrawer(Gravity.LEFT);
    }

}
