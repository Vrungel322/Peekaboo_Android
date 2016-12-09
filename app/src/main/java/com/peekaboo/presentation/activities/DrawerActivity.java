package com.peekaboo.presentation.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
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
import com.peekaboo.domain.usecase.UserModeChangerUseCase;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.adapters.HotFriendsAdapter;
import com.peekaboo.presentation.app.view.OnlineIndicatorView;
import com.peekaboo.presentation.dialogs.ChooseImageDialogFragment;
import com.peekaboo.presentation.fragments.SettingsFragment;
import com.peekaboo.presentation.listeners.DrawerListener;
import com.peekaboo.presentation.presenters.MainActivityPresenter;
import com.peekaboo.presentation.services.INotifier;
import com.peekaboo.presentation.services.Message;
import com.peekaboo.presentation.utils.ActivityUtils;
import com.peekaboo.presentation.utils.ImageUtils;
import com.peekaboo.presentation.utils.ResourcesUtils;
import com.peekaboo.presentation.views.IMainView;
import com.peekaboo.utils.ActivityNavigator;
import com.squareup.otto.Bus;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public abstract class DrawerActivity extends AppCompatActivity implements IMainView,
        ChooseImageDialogFragment.ChooseImageListener,
        INotifier.NotificationListener<Message>,
        SettingsFragment.IUpdateAvatarInDrawer {
    public static final int BLUR_RATE = 20;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
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
    @BindView(R.id.ivAvatarBlur)
    ImageView ivAvatarBlur;
    @BindView(R.id.pbLoading_avatar_progress_bar)
    ProgressBar pbLoading_avatar_progress_bar;
    @BindView(R.id.oiUserOnlineIndicator)
    OnlineIndicatorView oiOnlineIndicator;
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
    @Inject
    Bus eventBus;
    private HotFriendsAdapter hotFriendsAdapter;
    private final Set<OnBackPressListener> listeners = new HashSet<>();
    private Target avatarTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            final Bitmap blurredImage = ImageUtils.getBlurredImage(bitmap, BLUR_RATE, false);
            ivAccountAvatar.setImageBitmap(bitmap);
            ivAvatarBlur.setImageBitmap(blurredImage);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Log.e("animator", "bitmapFailed");
            ivAvatarBlur.setImageBitmap(null);

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            Log.e("animator", "prepareLoad");
        }
    };
    private Target avatarTargetAnimated = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            setAvatarWithBlur(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Log.e("animator", "bitmapFailed");
            ivAvatarBlur.setImageBitmap(null);

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            Log.e("animator", "prepareLoad");
        }
    };
    private DrawerLayout.DrawerListener drawerListener = new DrawerListener() {

        @Override
        public void onDrawerOpened(View drawerView) {
            presenter.fillHotAdapter();
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            DrawerActivity.this.onDrawerClosed();
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            if (newState > DrawerLayout.STATE_IDLE) {
                ActivityUtils.hideKeyboard(DrawerActivity.this);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        prepareDrawer();
        inject();
        eventBus.register(this);
        presenter.bind(this);
        updateAccountData(accountUser);
        prepareHotFriends();
        notifier.addListener(this);
        if (notifier.isAvailable()) {
            onConnected();
        } else {
            onDisconnected();
            notifier.tryConnect(accountUser.getBearer());
        }

        presenter.setUserModeListener(this::renderState);
    }

    protected abstract void prepareActionBar(Toolbar toolbar, DrawerLayout drawer);

    protected abstract void inject();

    protected void onDrawerClosed() {

    }

    protected abstract void handleDrawerClick(int id);

    private void renderState(byte type) {
        switch (type) {
            case UserModeChangerUseCase.IUserMode.TEXT_MODE:
                bText.setSelected(true);
                bAudio.setSelected(false);
                bVideo.setSelected(false);
                break;

            case UserModeChangerUseCase.IUserMode.AUDIO_MODE:
                bText.setSelected(false);
                bAudio.setSelected(true);
                bVideo.setSelected(false);
                break;

            case UserModeChangerUseCase.IUserMode.VIDEO_MODE:
                bText.setSelected(false);
                bAudio.setSelected(false);
                bVideo.setSelected(true);
                break;

            case UserModeChangerUseCase.IUserMode.ALL_MODE:
                bText.setSelected(true);
                bAudio.setSelected(true);
                bVideo.setSelected(true);
                break;
        }
    }


    private void prepareHotFriends() {
        hotFriendsAdapter = new HotFriendsAdapter(DrawerActivity.this, mPicasso, navigator);
        OverScrollDecoratorHelper.setUpOverScroll(lvHotFriends);
        lvHotFriends.setAdapter(hotFriendsAdapter);

    }

    @Override
    public void hotFriendToShow(List<Dialog> hotDialogs) {
        hotFriendsAdapter.setItems(hotDialogs);
    }

    @Override
    public void updateAvatarView(String result) {
        showProgress();
        if (result.equals("Ok")) {
            showAvatar(accountUser.getAvatarMiddle());
        } else {
            showToastMessage("Error in updating avatar... Sorryan");
        }
    }

    private void prepareDrawer() {
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            prepareActionBar(toolbar, drawer);
        }
        drawer.addDrawerListener(drawerListener);

    }


    private void updateAccountData(AccountUser accountUser) {
        byte mode = accountUser.getMode();
        String avatarUrl = accountUser.getAvatarMiddle();
        String userName = accountUser.getUsername();
        tvNameSurname.setText(userName);
        Log.e("animator", "avatarUrl " + avatarUrl);
        int avatarSize = ResourcesUtils.getDimenInPx(this, R.dimen.sizeOfIconInDrawer);
        Picasso.with(this).load(avatarUrl)
                .resize(0, avatarSize)
                .into(avatarTarget);

        renderState(mode);
    }


    private void showAvatar(String avatarUrl) {
        int avatarSize = ResourcesUtils.getDimenInPx(this, R.dimen.sizeOfIconInDrawer);
        Picasso.with(this).load(avatarUrl).memoryPolicy(MemoryPolicy.NO_CACHE)
                .resize(0, avatarSize)
                .into(avatarTargetAnimated);
    }

    @Override
    protected void onDestroy() {
        drawer.removeDrawerListener(drawerListener);
        notifier.removeListener(this);
        presenter.unbind();
        eventBus.unregister(this);
        super.onDestroy();
    }


    @OnClick(R.id.ivAccountAvatar)
    public void onAvatarClick() {
        DialogFragment newFragment = new ChooseImageDialogFragment();
        newFragment.show(getSupportFragmentManager(), ChooseImageDialogFragment.TAG);
    }

    @OnClick(R.id.llExit)
    public void onExitClick() {
        PeekabooApplication.getApp(this).logout();
    }

    @OnClick({R.id.llDialogs, R.id.llCalls, R.id.llContacts, R.id.llProfile, R.id.llSettings})
    public void onDrawerItemClick(View v) {
        selectionMode(v.getId());
        handleDrawerClick(v.getId());
        drawer.closeDrawer(Gravity.LEFT);
    }


    @OnClick({R.id.bText, R.id.bAudio, R.id.bVideo})
    public void onRadioButtonClicked(View v) {
        if (notifier.isAvailable()) {
            byte mode = (v.getId() == R.id.bText
                    ? UserModeChangerUseCase.IUserMode.TEXT_MODE : v.getId() == R.id.bAudio ?
                    UserModeChangerUseCase.IUserMode.AUDIO_MODE : UserModeChangerUseCase.IUserMode.ALL_MODE);
            presenter.setUserMode(mode);
        }
    }

    protected void selectionMode(int id) {
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

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.LEFT)) {
            drawer.closeDrawer(Gravity.LEFT);
        } else if (drawer.isDrawerOpen(Gravity.RIGHT)) {
            drawer.closeDrawer(Gravity.RIGHT);
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
        oiOnlineIndicator.setState(true, 0);
    }

    @Override
    public void onDisconnected() {
        oiOnlineIndicator.setState(false, 0);
    }

    @Override
    public void showToastMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {
//        pbLoading_avatar_progress_bar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
//        pbLoading_avatar_progress_bar.setVisibility(View.GONE);
    }

    @Override
    public void onImageChosen(String file) {
        Log.e("MainActivity", "image " + file);
        presenter.updateAvatar(file);
    }


    protected void changeFragment(Fragment fragment, @Nullable String tag) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment, tag)
                .commit();
    }

    @Override
    public void updateAvatarInDrawer() {
        showAvatar(accountUser.getAvatarMiddle());
    }

    public TabLayout getTabLayout() {
        return tabLayout;
    }

    public interface OnBackPressListener {
        boolean onBackPress();
    }

    private void setAvatarWithBlur(Bitmap bitmap) {
        final Bitmap blurredImage = ImageUtils.getBlurredImage(bitmap, BLUR_RATE, false);
        ivAccountAvatar.setImageBitmap(bitmap);
        AnimatorSet animatorSet = new AnimatorSet();
        Log.e("animator", "bitmapLoaded " + blurredImage + " " + bitmap);
        Animator hide = ObjectAnimator.ofFloat(ivAvatarBlur, "alpha", 0f).setDuration(200);
        hide.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Log.e("animator", "" + animation);
                ivAvatarBlur.setImageBitmap(blurredImage);
            }
        });
        Animator show = ObjectAnimator.ofFloat(ivAvatarBlur, "alpha", 1f).setDuration(200);
        animatorSet.playSequentially(hide, show);
        animatorSet.start();
    }
}