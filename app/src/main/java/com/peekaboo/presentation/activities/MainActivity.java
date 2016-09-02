package com.peekaboo.presentation.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etiennelawlor.discreteslider.library.ui.DiscreteSlider;
import com.etiennelawlor.discreteslider.library.utilities.DisplayUtility;
import com.peekaboo.R;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.fragments.CallsFragment;
import com.peekaboo.presentation.fragments.ContactsFragment;
import com.peekaboo.presentation.fragments.DialogsFragment;
import com.peekaboo.presentation.fragments.FriendTestFragment;
import com.peekaboo.presentation.fragments.ProfileFragment;
import com.peekaboo.presentation.fragments.SettingsFragment;
import com.peekaboo.presentation.fragments.SocketTestFragment;
import com.peekaboo.presentation.services.INotifier;
import com.peekaboo.presentation.services.Message;
import com.peekaboo.presentation.services.MessageUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

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

    @BindView(R.id.discrete_slider)
    DiscreteSlider discreteSlider;
    @BindView(R.id.rlSliderLabel)
    RelativeLayout rlSliderLabel;
    @Inject
    INotifier<Message> messanger;


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
        if (getSupportFragmentManager().findFragmentById(R.id.fragmentContainer) == null) {
            changeFragment(new FriendTestFragment(), null);
        }

//        changeFragment(new SocketTestFragment(), null);
//        changeFragment(new ServiceTestFragment(), null);
//        changeFragment(new RecordTestFragment(), null);


        discreteSlider.setOnDiscreteSliderChangeListener(new DiscreteSlider.OnDiscreteSliderChangeListener() {
            @Override
            public void onPositionChanged(int position) {
                Toast.makeText(getApplicationContext(), "pos : " + position, Toast.LENGTH_SHORT).show();
                int childCount = rlSliderLabel.getChildCount();
                if (messanger.isAvailable()) {
                    messanger.sendMessage(MessageUtils.createSwitchModeMessage((byte) position));
                }
                for (int i = 0; i < childCount; i++) {
                    TextView tv = (TextView) rlSliderLabel.getChildAt(i);
                    if (i == position)
                        tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                    else
                        tv.setTextColor(getResources().getColor(R.color.colorAccent));
                }
            }
        });

        rlSliderLabel.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rlSliderLabel.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                addTickMarkTextLabels();
            }
        });
    }

    @OnClick({R.id.llDialogs, R.id.llCalls, R.id.llContacts, R.id.llProfile, R.id.llSettings, R.id.llExit})
    public void onDrawerItemClick(View v) {
        selectionMode(v.getId());
        switch (v.getId()) {
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

    private void addTickMarkTextLabels() {
        int tickMarkCount = discreteSlider.getTickMarkCount();
        float tickMarkRadius = discreteSlider.getTickMarkRadius();
        int width = rlSliderLabel.getMeasuredWidth();

        int discreteSliderBackdropLeftMargin = DisplayUtility.dp2px(this, 10);
        int discreteSliderBackdropRightMargin = DisplayUtility.dp2px(this, 10);
        float firstTickMarkRadius = tickMarkRadius;
        float lastTickMarkRadius = tickMarkRadius;
        int interval = (width - (discreteSliderBackdropLeftMargin +
                discreteSliderBackdropRightMargin) -
                ((int) (firstTickMarkRadius + lastTickMarkRadius))) / (tickMarkCount);

        String[] tickMarkLabels = {"All", "Text", "Audio", "Video"};
        int tickMarkLabelWidth = DisplayUtility.dp2px(this, 40);

        for (int i = 0; i < tickMarkCount; i++) {
            TextView tv = new TextView(this);

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    tickMarkLabelWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);

            tv.setText(tickMarkLabels[i]);
            tv.setGravity(Gravity.CENTER);
            if (i == 0)
                tv.setTextColor(getResources().getColor(R.color.colorPrimary));
            else
                tv.setTextColor(getResources().getColor(R.color.colorAccent));

//                    tv.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));

            int left = discreteSliderBackdropLeftMargin + (int) firstTickMarkRadius * 6 + (i * interval);

            layoutParams.setMargins(left,
                    0,
                    0,
                    0);
            tv.setLayoutParams(layoutParams);

            rlSliderLabel.addView(tv);
        }
    }

}
