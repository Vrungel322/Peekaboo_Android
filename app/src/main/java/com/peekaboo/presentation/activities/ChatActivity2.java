package com.peekaboo.presentation.activities;

import android.animation.Animator;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.peekaboo.R;
import com.peekaboo.data.FileEntity;
import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.domain.subscribers.BaseUseCaseSubscriber;
import com.peekaboo.domain.usecase.UploadFileUseCase;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.adapters.ChatAdapter2;
import com.peekaboo.presentation.fragments.ChatItemDialog;
import com.peekaboo.presentation.listeners.ChatClickListener;
import com.peekaboo.presentation.listeners.ChatRecyclerTouchListener;
import com.peekaboo.presentation.presenters.ChatPresenter2;
import com.peekaboo.presentation.services.INotifier;
import com.peekaboo.presentation.services.Message;
import com.peekaboo.presentation.services.MessageUtils;
import com.peekaboo.presentation.views.IChatView2;
import com.peekaboo.utils.Constants;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.codetail.animation.ViewAnimationUtils;
import io.codetail.widget.RevealFrameLayout;

/**
 * Created by sebastian on 09.09.16.
 */
public class ChatActivity2 extends AppCompatActivity implements IChatView2 {
    public static final String COMPANION_ID = "companionId";
    @BindView(R.id.etMessageBody)
    EditText etMessageBody;
    @BindView(R.id.rvMessages)
    RecyclerView rvMessages;
    @BindView(R.id.flMessageBody)
    FrameLayout flMessageBody;
    @BindView(R.id.rflMessageBody)
    RevealFrameLayout rflMessageBody;
    @BindView(R.id.bMesageOpen)
    ImageButton bMessageOpen;
    @BindView(R.id.bSendMessage)
    ImageButton bSendMessage;
    @BindView(R.id.svItems)
    HorizontalScrollView svItems;
    @BindView(R.id.llItems)
    LinearLayout llItems;
    @Inject
    ChatPresenter2 presenter;
    @Inject
    AccountUser accountUser;
    @Inject
    UploadFileUseCase uploadFileUseCase;
    @Inject
    INotifier<Message> notifier;
    private ChatAdapter2 adapter;
    private boolean isFirstResumeAfterCreate = true;
    private String companionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        companionId = getIntent().getStringExtra(COMPANION_ID);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarChat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        PeekabooApplication.getApp(this).getComponent().inject(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        rvMessages.setLayoutManager(layoutManager);

        rvMessages.setItemAnimator(new DefaultItemAnimator());
        adapter = new ChatAdapter2(this, presenter, rvMessages);
        rvMessages.setAdapter(adapter);
        rvMessages.addOnItemTouchListener(new ChatRecyclerTouchListener(this, rvMessages, new ChatClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
                showItemMenu(position);
            }
        }));

        presenter.bind(this);
    }

    private void showItemMenu(int position) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment chatItemDialog = new ChatItemDialog();
        Bundle itemIndexBundle = new Bundle();
        itemIndexBundle.putInt(Constants.ARG_CHAT_MESSAGE_ITEM_INDEX, position);
        chatItemDialog.setArguments(itemIndexBundle);
        chatItemDialog.show(ft, Constants.FRAGMENT_TAGS.CHAT_ITEM_DIALOG_FRAGMENT_TAG);
    }

    @OnClick(R.id.bSendMessage)
    void onButtonSendCLick() {
        if (getMessageText().isEmpty()) {
            String fileName = "/sdcard/eric.wav";
            uploadFileUseCase.setInfo(fileName, getCompanionId());
            uploadFileUseCase.execute(new BaseUseCaseSubscriber<FileEntity>() {
                @Override
                public void onNext(FileEntity fileEntity) {
                    super.onNext(fileEntity);
                    Message typeMessage = MessageUtils.createTypeMessage(getCompanionId(), Message.Type.AUDIO, fileEntity.getName(), accountUser.getId());
                    notifier.sendMessage(typeMessage);
                }
            });
        } else {
            presenter.onSendTextButtonPress(getMessageText());
        }
//        presenter.onSendTextButtonPress(getMessageText());
    }

    @Override
    public void clearTextField() {
        etMessageBody.setText("");
    }

    @Override
    public String getMessageText() {
        return etMessageBody.getText().toString().trim().replaceAll("[\\s&&[^\r?\n]]+", " ");
    }


    @OnClick(R.id.bMesageOpen)
    void onbMessageOpenClick() {
        rflMessageBody.setVisibility(View.VISIBLE);
        etMessageBody.post(() -> {
            float cx, cy;
            cx = (bMessageOpen.getX() + bMessageOpen.getWidth()) / 2;
            cy = (bMessageOpen.getY() + bMessageOpen.getHeight()) / 2;

            float dx = Math.max(cx, flMessageBody.getWidth() - cx);
            float dy = Math.max(cy, flMessageBody.getHeight() - cy);
            float finalRadius = (float) Math.hypot(dx, dy);

            Animator animator =
                    ViewAnimationUtils.createCircularReveal(flMessageBody, (int) cx, (int) cy, 0, finalRadius);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(300);
            animator.start();
        });

        bMessageOpen.setVisibility(View.GONE);
        LinearLayout.LayoutParams layoutParamsLlItems = (LinearLayout.LayoutParams) llItems.getLayoutParams();

        layoutParamsLlItems.leftMargin = 0;

        llItems.setLayoutParams(layoutParamsLlItems);

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume(isFirstResumeAfterCreate, getCompanionId());
        isFirstResumeAfterCreate = false;
    }

    @Override
    protected void onPause() {
        presenter.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        presenter.unbind();
        super.onDestroy();
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
        return companionId;
    }

    @Override
    public void showToastMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }
}
