package com.peekaboo.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.peekaboo.R;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.adapters.HotFriendsAdapter;
import com.peekaboo.presentation.fragments.CallsFragment;
import com.peekaboo.presentation.fragments.ContactsFragment;
import com.peekaboo.presentation.fragments.DialogsFragment;
import com.peekaboo.presentation.fragments.FriendTestFragment;
import com.peekaboo.presentation.fragments.ProfileFragment;
import com.peekaboo.presentation.fragments.SettingsFragment;
import com.peekaboo.presentation.pojo.HotFriendPOJO;
import com.peekaboo.presentation.services.INotifier;
import com.peekaboo.presentation.services.Message;
import com.peekaboo.presentation.services.MessageUtils;
import com.peekaboo.presentation.utils.ResourcesUtils;
import com.peekaboo.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class MainActivity extends AppCompatActivity implements INotifier.NotificationListener<Message> {
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.bText)
    Button bText;
    @BindView(R.id.bAudio)
    Button bAudio;
    @BindView(R.id.bVideo)
    Button bVideo;
    @BindView(R.id.lvHotFriends)
    ListView lvHotFriends;
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
    @BindView(R.id.tvNameSurname)
    TextView tvNameSurname;
    @BindView(R.id.ivAccountAvatar)
    ImageView ivAccountAvatar;
    @BindView(R.id.ivOnlineStatus)
    ImageView ivOnlineStatus;
    @Inject
    INotifier<Message> notifier;
    @Inject
    AccountUser accountUser;
    private HotFriendsAdapter hotFriendsAdapter;
    private ArrayList<HotFriendPOJO> alHotFriendPOJO;
    private final Set<OnBackPressListener> listeners = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        PeekabooApplication.getApp(this).getComponent().inject(this);
        prepareDrawer();
        updateAccountData(accountUser);

        if (getSupportFragmentManager().findFragmentById(R.id.fragmentContainer) == null) {
            changeFragment(new FriendTestFragment(), null);
        }
        //Hardcode list in right drawer
        prepareHotFriends();

        notifier.addListener(this);
        if (notifier.isAvailable()) {
            onConnected();
        } else {
            onDisconnected();
            notifier.tryConnect(accountUser.getBearer());
        }
    }

    private void prepareHotFriends() {
        alHotFriendPOJO = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            alHotFriendPOJO.add(new HotFriendPOJO(R.drawable.raccoon, Math.random() < 0.5));
        }
        hotFriendsAdapter = new HotFriendsAdapter(getApplicationContext(), alHotFriendPOJO);
        OverScrollDecoratorHelper.setUpOverScroll(lvHotFriends);
        lvHotFriends.setAdapter(hotFriendsAdapter);
        lvHotFriends.setOnItemClickListener((parent, view, position, id) -> {
            startActivity(new Intent(MainActivity.this, ChatActivity.class));
            drawer.closeDrawer(Gravity.RIGHT, true);
        });
    }

    private void prepareDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeButtonEnabled(true);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void updateAccountData(AccountUser accountUser) {
        int mode = accountUser.getMode();
        String avatarUrl = accountUser.getAvatar();
        String userName = accountUser.getUsername();
        tvNameSurname.setText(userName);
        Log.e("activity", "" + avatarUrl);
        int avatarSize = ResourcesUtils.getDimenInPx(this, R.dimen.widthOfIconInDrawer);
        Picasso.with(this).load(avatarUrl)
                .resize(0, avatarSize)
                .into(ivAccountAvatar);

        bText.setSelected(mode == 1);
        bAudio.setSelected(mode == 2);
        bVideo.setSelected(mode == 0);
    }

    @Override
    protected void onDestroy() {
        notifier.removeListener(this);
        super.onDestroy();
    }

    @OnClick({R.id.llDialogs, R.id.llCalls, R.id.llContacts, R.id.llProfile, R.id.llSettings, R.id.llExit})
    public void onDrawerItemClick(View v) {
        selectionMode(v.getId());
        switch (v.getId()) {
            case R.id.llDialogs:
                changeFragment(new DialogsFragment(), Constants.FRAGMENT_TAGS.DIALOGS_FRAGMENT);
                break;
            case R.id.llCalls:
                changeFragment(new CallsFragment(), Constants.FRAGMENT_TAGS.CALLS_FRAGMENT);
                break;
            case R.id.llContacts:
                changeFragment(new ContactsFragment(), Constants.FRAGMENT_TAGS.CONTACTS_FRAGMENT);
                break;
            case R.id.llProfile:
                changeFragment(new ProfileFragment(), Constants.FRAGMENT_TAGS.PROFILE_FRAGMENT);
                break;
            case R.id.llSettings:
                changeFragment(new SettingsFragment(), Constants.FRAGMENT_TAGS.SETTINGS_FRAGMENT);
                break;
            case R.id.llExit:
//                throw new RuntimeException();
        }

    }

    @OnClick({R.id.bText, R.id.bAudio, R.id.bVideo})
    public void onRadioButtonClicked(View v) {
        if (notifier.isAvailable()) {
            bText.setSelected(v.getId() == R.id.bText);
            bAudio.setSelected(v.getId() == R.id.bAudio);
            bVideo.setSelected(v.getId() == R.id.bVideo);
            byte mode = (byte) (v.getId() == R.id.bText ? 1 : v.getId() == R.id.bAudio ? 2 : 0);
            Message switchModeMessage = MessageUtils.createSwitchModeMessage(mode);
            notifier.sendMessage(switchModeMessage);
            accountUser.saveMode(mode);
        }
    }

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

    @Override
    public void onBackPressed() {
        boolean callSuper = true;
        for (OnBackPressListener listener : listeners) {
            if (listener.onBackPress()) {
                callSuper = false;
            }
        }
        if (callSuper) {
            super.onBackPressed();
        }
    }

    public void addListener(OnBackPressListener listener) {
        listeners.add(listener);
    }

    public void removeListener(OnBackPressListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onMessageObtained(Message message) {

    }

    @Override
    public void onConnected() {
        ivOnlineStatus.setImageResource(R.drawable.round_status_icon_cyan);
    }

    @Override
    public void onDisconnected() {
        ivOnlineStatus.setImageResource(R.drawable.round_status_icon_grey);
    }

    public interface OnBackPressListener {
        boolean onBackPress();
    }
}
