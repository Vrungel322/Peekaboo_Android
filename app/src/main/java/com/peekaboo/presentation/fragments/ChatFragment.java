package com.peekaboo.presentation.fragments;

import android.animation.Animator;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.peekaboo.R;
import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.activities.MainActivity;
import com.peekaboo.presentation.activities.MapActivity;
import com.peekaboo.presentation.adapters.ChatAdapter2;
import com.peekaboo.presentation.app.view.PHorizontalScrollView;
import com.peekaboo.presentation.presenters.ChatPresenter2;
import com.peekaboo.presentation.services.INotifier;
import com.peekaboo.presentation.services.Message;
import com.peekaboo.presentation.utils.ActivityUtils;
import com.peekaboo.presentation.views.IChatView2;
import com.peekaboo.utils.ActivityNavigator;
import com.peekaboo.utils.Constants;
import com.peekaboo.utils.IntentUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTouch;
import io.codetail.animation.ViewAnimationUtils;
import io.codetail.widget.RevealFrameLayout;

/**
 * Created by sebastian on 09.09.16.
 */
public class ChatFragment extends Fragment implements IChatView2, MainActivity.OnBackPressListener {
    public static final String COMPANION = "companion";
    public static final String IMAGE_FILE = "image_file";
    @BindView(R.id.etMessageBody)
    EditText etMessageBody;
    @BindView(R.id.rvMessages)
    RecyclerView rvMessages;
    @BindView(R.id.llMessageBody)
    LinearLayout llMessageBody;
    @BindView(R.id.rflMessageBody)
    RevealFrameLayout rflMessageBody;
    @BindView(R.id.bMessageOpen)
    ImageButton bMessageOpen;
    @BindView(R.id.bSendMessage)
    ImageButton bSendMessage;
    @BindView(R.id.svItems)
    PHorizontalScrollView svItems;
    @BindView(R.id.llItems)
    LinearLayout llItems;
    @BindView(R.id.micro_btn)
    ImageButton bRecord;
    @BindView(R.id.micro_anim)
    ImageView microAnim;
    @BindView(R.id.rflButtonRecord)
    RevealFrameLayout rflButtonRecord;
    @BindView(R.id.llRecord)
    LinearLayout llRecord;
    @BindView(R.id.flTimer)
    FrameLayout flTimer;
    @BindView(R.id.timerRecord)
    Chronometer timerRecord;
    @BindView(R.id.rflTimer)
    RevealFrameLayout rflTimer;
    ////////
    @BindView(R.id.navigation_btn)
    ImageButton bNavigation;

    ////////
    @BindView(R.id.pbLoadingImageToServer)
    ProgressBar pbLoadingImageToServer;
    @Inject
    ChatPresenter2 presenter;
    @Inject
    AccountUser accountUser;
    @Inject
    INotifier<Message> notifier;
    @Inject
    ActivityNavigator activityNavigator;
    @Inject
    Picasso mPicasso;
    private ChatAdapter2 adapter;
    private LinearLayout.LayoutParams layoutParams;
    private boolean isFirstResumeAfterCreate = true;
    private Contact companion;
    private String imageFile;

    private Animator animator;
    private ChatItemDialog chatItemDialog;
    private ChatItemDialog.IChatItemEventListener iChatItemEventListener;

    public static ChatFragment newInstance(Contact companion) {

        ChatFragment fragment = new ChatFragment();

        Bundle args = new Bundle();
        args.putParcelable(COMPANION, companion);
        fragment.setArguments(args);
        return fragment;
    }

    public interface DISABLE_pbLoadingImageToServer {
        void disablePbLoadingImageToServer();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PeekabooApplication.getApp(getActivity()).getComponent().inject(this);
        companion = getArguments().getParcelable(COMPANION);
        restoreState(savedInstanceState);
    }


    private void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            imageFile = savedInstanceState.getString(IMAGE_FILE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(companion.contactNickname());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_layout, container, false);
        ButterKnife.bind(this, view);

        layoutParams = (LinearLayout.LayoutParams) rflMessageBody.getLayoutParams();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        rvMessages.setLayoutManager(layoutManager);
        rvMessages.setItemAnimator(new DefaultItemAnimator());
        adapter = new ChatAdapter2(getActivity(), presenter, rvMessages, companion, mPicasso);
        rvMessages.setAdapter(adapter);
        svItems.setOnTouchListener((view1, motionEvent) -> false);
        rvMessages.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState > RecyclerView.SCROLL_STATE_IDLE) {
                    ActivityUtils.hideKeyboard(getActivity());
                }
            }
        });
        adapter.setOnItemLongClickListener((position) -> {
            android.support.v4.app.FragmentTransaction ft = getActivity()
                    .getSupportFragmentManager().beginTransaction();
            chatItemDialog = new ChatItemDialog();
            Bundle itemIndexBundle = new Bundle();
            chatItemDialog.setChatItemEventListener(new ChatItemDialog.IChatItemEventListener() {
                @Override
                public void copyText(int index) {
                    presenter.onCopyMessageTextClick((ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE),
                            adapter.getItem(index));
                }

//        rvMessages.addOnItemTouchListener(new ChatRecyclerTouchListener(getActivity(), rvMessages, new ChatClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                if (adapter.getItemViewType(position) == PMessageAbs.PMESSAGE_MEDIA_TYPE.IMAGE_MESSAGE) {
//                    PreviewImageFragment previewImageFragment = new PreviewImageFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString(Constants.FILEPATH_OF_IMAGE_TO_PREVIEW,
//                             ResourcesUtils.splitImagePath(adapter.getItem(position).messageBody(), 2));
//                    previewImageFragment.setArguments(bundle);
//                    previewImageFragment.show(getFragmentManager(), Constants.FRAGMENT_TAGS.PREVIEW_IMAGE_FRAGMENT);
//                }
//            }

//            @Override
//            public void onLongClick(View view, int position) {
//                android.support.v4.app.FragmentTransaction ft = ((AppCompatActivity) getActivity())
//                        .getSupportFragmentManager().beginTransaction();
//                chatItemDialog = new ChatItemDialog();
//                Bundle itemIndexBundle = new Bundle();
//                chatItemDialog.setChatItemEventListener(new ChatItemDialog.IChatItemEventListener() {
//                    @Override
//                    public void copyText(int index) {
//                        presenter.onCopyMessageTextClick((ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE),
//                                adapter.getItem(index));
//                    }
//>>>>>>> development

                @Override
                public void deleteMess(int index) {
                    presenter.onDeleteMessageClick(adapter.getItem(index));
                    presenter.showUpdatedMessages(getCompanionId());
                }

                @Override
                public void textToSpeech(int index) {
                    presenter.onConvertTextToSpeechClick(adapter.getItem(index));

                }
            });
            itemIndexBundle.putInt(Constants.ARG_CHAT_MESSAGE_ITEM_INDEX, position);
            chatItemDialog.setArguments(itemIndexBundle);
            chatItemDialog.show(ft, Constants.FRAGMENT_TAGS.CHAT_ITEM_DIALOG_FRAGMENT_TAG);


        });
        presenter.bind(this);
        return view;
    }

    @OnClick(R.id.bSendMessage)
    void onButtonSendClick() {
        presenter.onSendTextButtonPress(getMessageText());
    }

    @Override
    public void clearTextField() {
        etMessageBody.setText("");
    }

    @Override
    public String getMessageText() {
        return etMessageBody.getText().toString().trim().replaceAll("[\\s&&[^\r?\n]]+", " ");
    }

    @OnTouch(R.id.micro_btn)
    boolean onRecordButtonClick(MotionEvent mv) {
        switch (mv.getAction()) {
            case MotionEvent.ACTION_DOWN:
                svItems.setScrollAvailable(false);
                showRecordStart();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                svItems.setScrollAvailable(true);
                presenter.onRecordButtonClick(false);
                showRecordStop();
                break;
        }
        return true;
    }

    @OnClick(R.id.bMessageOpen)
    void onbMessageOpenClick() {
        rflMessageBody.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            etMessageBody.post(() -> {
                float cx, cy;
                cx = (bMessageOpen.getX() + bMessageOpen.getWidth()) / 2;
                cy = (bMessageOpen.getY() + bMessageOpen.getHeight()) / 2;

                float dx = Math.max(cx, llMessageBody.getWidth() - cx);
                float dy = Math.max(cy, llMessageBody.getHeight() - cy);
                float finalRadius = (float) Math.hypot(dx, dy);

                Animator animator =
                        ViewAnimationUtils.createCircularReveal(llMessageBody, (int) cx, (int) cy, 0, finalRadius);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(300);
                animator.start();
            });
        }

        bMessageOpen.setVisibility(View.GONE);
        bSendMessage.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams layoutParamsLlItems = (LinearLayout.LayoutParams) llItems.getLayoutParams();

        layoutParamsLlItems.leftMargin = 0;

        llItems.setLayoutParams(layoutParamsLlItems);

    }

    @OnClick(R.id.photo_btn)
    void onCameraButtonClick() {
        takePhoto();
    }

    @OnClick(R.id.gallery_btn)
    void onGalleryButtonClick() {
        takeGalleryImage();
    }

    public void takeGalleryImage() {
        boolean canTake = IntentUtils.takeGalleryImage(this);
        if (!canTake) {
            showToastMessage(getString(R.string.galleryIsNotAvailable));
        }
    }

    public void takePhoto() {
        imageFile = IntentUtils.capturePhoto(this);
        if (imageFile == null) {
            showToastMessage(getString(R.string.cameraIsNotAvailable));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(IMAGE_FILE, imageFile);
        super.onSaveInstanceState(outState);
    }

    @OnClick(R.id.navigation_btn)
    void onNavigationButtonClick() {
        takeNavigation();
    }

    public void takeNavigation() {
//        Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194?z=20");
//        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//        mapIntent.setPackage("com.google.android.apps.maps");
//        startActivity(mapIntent);
//        ---
        Intent mapintent = new Intent(getActivity(), MapActivity.class);
        getActivity().startActivityForResult(mapintent, Constants.REQUEST_CODES.REQUEST_CODE_GPS);
        Log.wtf("NULL : ", "sendim gpsimg in fragment");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IntentUtils.CAMERA_REQUEST_CODE:
            case IntentUtils.GALLERY_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    if (imageFile == null) {
                        imageFile = IntentUtils.onGalleryActivityResult(getActivity(), requestCode, resultCode, data);
                    }
                    Log.e("ChatFragment", "onActivityResult() " + imageFile);
                    if (imageFile != null) {
                        sendImage(imageFile);
                        imageFile = null;
                    }
                } else {
                    imageFile = null;
                }
                break;
            case Constants.REQUEST_CODES.REQUEST_CODE_GPS:
                if (resultCode == Activity.RESULT_OK && null != data) {
                    String link = data.getStringExtra("staticmap");
//                    Toast.makeText(getContext(), link, Toast.LENGTH_SHORT).show();
                    Log.wtf("NULL : ", "sendImage " + link);
                    sendGeo(link);

                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }

    }

    public boolean sendGeo(String link) {
        Log.wtf("NULL : ", "sendGeo " + link);
        if (link == null) {
            return false;
        }
        presenter.onSendGPSButtonPress(link);
        return true;
        }

    public void sendImage(String imageFile) {
        pbLoadingImageToServer.setVisibility(View.VISIBLE);
        presenter.onSendImageButtonPress(imageFile);
//        Utility.galleryAddPic(getContext(), uri);
//        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).addListener(this);
        presenter.onResume(isFirstResumeAfterCreate, getCompanionId());
        isFirstResumeAfterCreate = false;
    }

    @Override
    public void onStop() {
        presenter.onPause();
        ((MainActivity) getActivity()).removeListener(this);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        presenter.unbind();
        super.onDestroyView();
    }

    @Override
    public void showMessages(List<PMessage> messages) {
        adapter.setMessages(messages);
    }

    @Override
    public void appendMessages(List<PMessage> messages) {
        adapter.appendMessages(messages);
    }

    @Override
    public void updateMessage(PMessage message) {
        adapter.updateMessage(message);
    }

    @Override
    public String getCompanionId() {
        return companion.contactId();
    }

    @Override
    public void showRecordStart() {
        int[] button_coordinates = new int[2];
        bRecord.getLocationOnScreen(button_coordinates);

        float cx, cy;
        cx = (float) button_coordinates[0] + bRecord.getWidth() / 2;
        cy = (float) button_coordinates[1] + bRecord.getHeight() / 2;

        float dx = Math.max(cx, rflTimer.getWidth() - cx);
        float dy = Math.max(cy, rflTimer.getHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);

        timerRecord.post(() -> {

            animator =
                    ViewAnimationUtils.createCircularReveal(flTimer, (int) cx, (int) cy, 0, finalRadius);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(600);
            rflTimer.setVisibility(View.VISIBLE);
            animator.start();
            animator.addListener(new Animator.AnimatorListener() {
                boolean cancelled;

                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (!cancelled) {
                        presenter.onRecordButtonClick(true);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    cancelled = true;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        });


        timerRecord.setOnChronometerTickListener(chronometer -> {
            long elapsedMillis = SystemClock.elapsedRealtime()
                    - timerRecord.getBase();
        });
        timerRecord.setBase(SystemClock.elapsedRealtime());

        timerRecord.start();

    }

    @Override
    public void showRecordStop() {
        etMessageBody.setFocusableInTouchMode(true);
        etMessageBody.setFocusable(true);
        rflTimer.setVisibility(View.GONE);
        if (animator != null) {
            animator.cancel();
            animator = null;
        }
    }

    @Override
    public void hidePbLoadingImageToServer() {
        pbLoadingImageToServer.setVisibility(View.GONE);
    }

    @Override
    public void showToastMessage(String text) {
        Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    @OnFocusChange(R.id.etMessageBody)
    public void onEditTextTouched() {

        if (etMessageBody.hasFocus()) {
            bSendMessage.setBackgroundResource(R.drawable.plane_blue);
            layoutParams.weight = 12;
        } else {
            bSendMessage.setBackgroundResource(R.drawable.plane);
            layoutParams.weight = 4;
        }
        rflMessageBody.setLayoutParams(layoutParams);
    }

    @Override
    public boolean onBackPress() {
        if (bMessageOpen.getVisibility() == View.VISIBLE) {
            return false;
        } else {
            bMessageOpen.setVisibility(View.VISIBLE);
            bSendMessage.setVisibility(View.GONE);
            rflMessageBody.setVisibility(View.GONE);
            return true;
        }
    }
}