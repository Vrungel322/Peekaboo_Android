package com.peekaboo.presentation.activities;

import android.animation.Animator;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import android.widget.Toast;

import com.peekaboo.R;
import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.adapters.ChatAdapter2;
import com.peekaboo.presentation.app.view.PHorizontalScrollView;
import com.peekaboo.presentation.listeners.ChatClickListener;
import com.peekaboo.presentation.listeners.ChatRecyclerTouchListener;
import com.peekaboo.presentation.presenters.ChatPresenter2;
import com.peekaboo.presentation.services.INotifier;
import com.peekaboo.presentation.services.Message;
import com.peekaboo.presentation.views.IChatView2;

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
    @Inject
    ChatPresenter2 presenter;
    @Inject
    AccountUser accountUser;
    @Inject
    INotifier<Message> notifier;
    private ChatAdapter2 adapter;
    private LinearLayout.LayoutParams layoutParams;
    private boolean isFirstResumeAfterCreate = true;
    private Contact companion;

    private Animator animator;

    public static ChatFragment newInstance(Contact companion) {

        ChatFragment fragment = new ChatFragment();

        Bundle args = new Bundle();
        args.putParcelable(COMPANION, companion);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PeekabooApplication.getApp(getActivity()).getComponent().inject(this);
        companion = getArguments().getParcelable(COMPANION);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((Toolbar) getActivity().findViewById(R.id.toolbar)).setTitle(companion.contactNickname());
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
        adapter = new ChatAdapter2(getActivity(), presenter, rvMessages);
        rvMessages.setAdapter(adapter);
        svItems.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        rvMessages.addOnItemTouchListener(new ChatRecyclerTouchListener(getActivity(), rvMessages, new ChatClickListener() {
            @Override
            public void onClick(View view, int position) {
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
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
        Log.e("fragment", "" + mv);
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

        bMessageOpen.setVisibility(View.GONE);
        bSendMessage.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams layoutParamsLlItems = (LinearLayout.LayoutParams) llItems.getLayoutParams();

        layoutParamsLlItems.leftMargin = 0;

        llItems.setLayoutParams(layoutParamsLlItems);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).addListener(this);
        presenter.onResume(isFirstResumeAfterCreate, getCompanionId());
        isFirstResumeAfterCreate = false;
    }

    @Override
    public void onPause() {
        presenter.onPause();
        ((MainActivity) getActivity()).removeListener(this);
        super.onPause();
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
        }
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