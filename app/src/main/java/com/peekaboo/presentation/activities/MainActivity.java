package com.peekaboo.presentation.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.peekaboo.R;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.fragments.CallsFragment;
import com.peekaboo.presentation.fragments.ContactsFragment;
import com.peekaboo.presentation.fragments.DialogsFragment;
import com.peekaboo.presentation.fragments.ProfileFragment;
import com.peekaboo.presentation.fragments.SettingsFragment;
import com.peekaboo.presentation.fragments.SocketTestFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.rgStateChanger)
    RadioGroup rgStateChanger;
    @BindView(R.id.llDialogs)
    LinearLayout llDialogs;
    @BindView(R.id.llCalls)
    LinearLayout llCalls;
    @BindView(R.id.llContacts)
    LinearLayout llContacts;
    @BindView(R.id.llProfile)
    LinearLayout llProfile;
    @BindView(R.id.llSettings)
    LinearLayout llSettings;
    @BindView(R.id.llExit)
    LinearLayout llExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        PeekabooApplication.getApp(this).getComponent().inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        changeFragment(new SocketTestFragment(), null);
//        changeFragment(new ServiceTestFragment(), null);
//        changeFragment(new RecordTestFragment(), null);
    }

    @OnClick({R.id.llDialogs, R.id.llCalls, R.id.llContacts, R.id.llProfile, R.id.llSettings, R.id.llExit})
    public void onDrawerItemClick(View v) {
        selectionMode(v.getId());
        switch (v.getId()){
            case R.id.llDialogs:
                changeFragment(new DialogsFragment(), "dialogsFragment");//new dialogsfragment
                break;
            case R.id.llCalls:
                changeFragment(new CallsFragment(), "callsFragment");
                break;
            case R.id.llContacts:
                changeFragment(new ContactsFragment(), "contactsFragment");
                break;
            case R.id.llProfile:
                changeFragment(new ProfileFragment(), "profileFragment");
                break;
            case R.id.llSettings:
                changeFragment(new SettingsFragment(), "settingsFragment");
                break;

        }

    }

    @OnClick({R.id.rbAll, R.id.rbText, R.id.rbAudio, R.id.rbVideo})
    public void onRadioButtonClicked(View v){
        switch (v.getId()){
            case R.id.rbAll:
                Toast.makeText(getApplicationContext(), "All", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rbText:
                Toast.makeText(getApplicationContext(), "Text", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rbAudio:
                Toast.makeText(getApplicationContext(), "Audio", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rbVideo:
                Toast.makeText(getApplicationContext(), "Video", Toast.LENGTH_SHORT).show();
                break;

        }
    }
//
//    @OnClick(R.id.llExit)
//    public void lvExitClick() {
//        Toast.makeText(getApplicationContext(), "Log out", Toast.LENGTH_LONG).show();
//    }

    private void selectionMode(int id) {
        llDialogs.setSelected(false);
        llCalls.setSelected(false);
        llContacts.setSelected(false);
        llProfile.setSelected(false);
        llSettings.setSelected(false);
        llExit.setSelected(false);
        switch (id) {
            case R.id.llDialogs:
                llDialogs.setSelected(true);
                break;
            case R.id.llCalls:
                llCalls.setSelected(true);
                break;
            case R.id.llContacts:
                llContacts.setSelected(true);
                break;
            case R.id.llProfile:
                llProfile.setSelected(true);
                break;
            case R.id.llSettings:
                llSettings.setSelected(true);
                break;
            case R.id.llExit:
                llExit.setSelected(true);
                break;
        }
    }

    private void changeFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment, tag)
                .commit();
        drawer.closeDrawer(Gravity.LEFT);
    }
}
