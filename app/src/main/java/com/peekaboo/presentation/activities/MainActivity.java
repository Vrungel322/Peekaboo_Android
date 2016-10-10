package com.peekaboo.presentation.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.peekaboo.R;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.domain.Dialog;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.adapters.HotFriendsAdapter;
import com.peekaboo.presentation.dialogs.AvatarChangeDialog;
import com.peekaboo.presentation.fragments.CallsFragment;
import com.peekaboo.presentation.fragments.ContactsFragment;
import com.peekaboo.presentation.fragments.DialogsFragment;
import com.peekaboo.presentation.fragments.ProfileFragment;
import com.peekaboo.presentation.fragments.SettingsFragment;
import com.peekaboo.presentation.pojo.HotFriendPOJO;
import com.peekaboo.presentation.presenters.MainActivityPresenter;
import com.peekaboo.presentation.services.INotifier;
import com.peekaboo.presentation.services.Message;
import com.peekaboo.presentation.services.MessageUtils;
import com.peekaboo.presentation.utils.ResourcesUtils;
import com.peekaboo.presentation.views.IMainView;
import com.peekaboo.utils.ActivityNavigator;
import com.peekaboo.utils.Constants;
import com.peekaboo.utils.Utility;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class MainActivity extends AppCompatActivity implements IMainView, AvatarChangeDialog.IAvatarChangeListener, INotifier.NotificationListener<Message>{
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
    @BindView(R.id.pbLoading_avatar_progress_bar)
    ProgressBar pbLoading_avatar_progress_bar;
    @BindView(R.id.ivOnlineStatus)
    ImageView ivOnlineStatus;

    @Inject
    INotifier<Message> notifier;
    @Inject
    AccountUser accountUser;
    @Inject
    MainActivityPresenter presenter;
    @Inject
    Picasso mPicasso;
    @Inject
    ActivityNavigator navigator;
    private HotFriendsAdapter hotFriendsAdapter;
    private ArrayList<HotFriendPOJO> alHotFriendPOJO;
    private final Set<OnBackPressListener> listeners = new HashSet<>();
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        PeekabooApplication.getApp(this).getComponent().inject(this);
        presenter.bind(this);
        prepareDrawer();
        updateAccountData(accountUser);

        if (getSupportFragmentManager().findFragmentById(R.id.fragmentContainer) == null) {
            changeFragment(ContactsFragment.newInstance(), Constants.FRAGMENT_TAGS.CONTACTS_FRAGMENT);
            selectionMode(R.id.llContacts);
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
        hotFriendsAdapter = new HotFriendsAdapter(MainActivity.this, mPicasso, navigator);
        OverScrollDecoratorHelper.setUpOverScroll(lvHotFriends);
        lvHotFriends.setAdapter(hotFriendsAdapter);

        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {}

            @Override
            public void onDrawerOpened(View drawerView) {
                presenter.fillHotAdapter();
            }

            @Override
            public void onDrawerClosed(View drawerView) {}

            @Override
            public void onDrawerStateChanged(int newState) {}
        });
    }

    @Override
    public void hotFriendToShow(List<Dialog> hotDialogs) {
        hotFriendsAdapter.setItems(hotDialogs);
    }

    @Override
    public void updateAvatarView(String result) {
        showProgress();
        if (result.equals("Ok")) {
            showAvatar(accountUser.getAvatar());
        } else {
            showToastMessage("Error in updating avatar... Sorryan");
        }
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

    private void showAvatar(String avatarUrl) {
        int avatarSize = ResourcesUtils.getDimenInPx(this, R.dimen.widthOfIconInDrawer);
        Picasso.with(this).load(avatarUrl).memoryPolicy(MemoryPolicy.NO_CACHE)
                .resize(0, avatarSize)
                .into(ivAccountAvatar, new Callback() {
                    @Override
                    public void onSuccess() {
                        hideProgress();
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        notifier.removeListener(this);
        presenter.unbind();
        super.onDestroy();
    }

    @OnClick({R.id.llDialogs, R.id.llCalls, R.id.llContacts, R.id.llProfile, R.id.llSettings, R.id.llExit, R.id.ivAccountAvatar})
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
                changeFragment(ContactsFragment.newInstance(), Constants.FRAGMENT_TAGS.CONTACTS_FRAGMENT);
                break;
            case R.id.llProfile:
                changeFragment(new ProfileFragment(), Constants.FRAGMENT_TAGS.PROFILE_FRAGMENT);
                break;
            case R.id.llSettings:
                changeFragment(new SettingsFragment(), Constants.FRAGMENT_TAGS.SETTINGS_FRAGMENT);
                break;
            case R.id.ivAccountAvatar:
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                AvatarChangeDialog avatarChangeDialog = new AvatarChangeDialog();
                avatarChangeDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
//        confirmSignUpDialog.setStyle(android.app.DialogFragment.STYLE_NO_FRAME, 0);
                avatarChangeDialog.show(ft, "avatar_change_dialog");
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
        if (drawer.isDrawerOpen(Gravity.LEFT)) {
            drawer.closeDrawer(Gravity.LEFT);
        } else {
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

    @Override
    public void showToastMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {
        pbLoading_avatar_progress_bar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        pbLoading_avatar_progress_bar.setVisibility(View.GONE);
    }

    public interface OnBackPressListener {
        boolean onBackPress();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODES.REQUEST_CODE_CAMERA) {
            if (resultCode == RESULT_OK) {
                ivAccountAvatar.setImageURI(imageUri);
                presenter.updateAvatar(getAvatarUri());
            }
        }
        if (requestCode == Constants.REQUEST_CODES.REQUEST_CODE_GALERY) {
            if (resultCode == RESULT_OK && null != data) {
                imageUri = data.getData();
//                Bitmap bitmap = null;
//                try {
//                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                ivAccountAvatar.setImageBitmap(bitmap);
                presenter.updateAvatar(data.getData());
            }
        }
    }

    @Override
    public void takePhoto(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = Utility.createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                imageUri = Utility.getImageContentUri(MainActivity.this, photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, Constants.REQUEST_CODES.REQUEST_CODE_CAMERA);
            }
        }
    }

    @Override
    public void takeFromGallery(){
        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                Constants.REQUEST_CODES.REQUEST_CODE_GALERY);
    }

    private Uri getAvatarUri(){
        if(imageUri != null){
            return imageUri;
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap bitmap = null;
        if (requestCode == Constants.REQUEST_CODES.REQUEST_CODE_CAMERA) {

            if (resultCode == RESULT_OK) {
//                try {
//                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                ivAccountAvatar.setImageBitmap(bitmap);

            }
        }
        if (requestCode == Constants.REQUEST_CODES.REQUEST_CODE_GALERY) {
            if (resultCode == RESULT_OK && null != data) {
//                imageUri = data.getData();
//                    try {
//                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    ivAccountAvatar.setImageBitmap(bitmap);
            }
        }
    }
}
