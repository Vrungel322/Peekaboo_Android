package com.peekaboo.presentation.activities;

import android.animation.Animator;
import android.app.FragmentTransaction;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.peekaboo.R;
import com.peekaboo.data.repositories.database.messages.TextPMessage;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.adapters.ChatAdapter;
import com.peekaboo.presentation.fragments.AttachmentChatDialog;
import com.peekaboo.presentation.fragments.ChatItemDialog;
import com.peekaboo.presentation.listeners.ChatClickListener;
import com.peekaboo.presentation.listeners.ChatRecyclerTouchListener;
import com.peekaboo.presentation.presenters.ChatPresenter;
import com.peekaboo.presentation.views.IChatView;
import com.peekaboo.utils.Constants;
import com.peekaboo.utils.Utility;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnItemSelected;
import butterknife.OnTouch;
import io.codetail.animation.ViewAnimationUtils;
import io.codetail.widget.RevealFrameLayout;
import timber.log.Timber;

/**
 * Created by Nataliia on 13.07.2016.
 */
public class ChatActivity extends AppCompatActivity
                        implements ChatItemDialog.IChatItemEventListener,
                                  ChatAdapter.IChatAdapterListener, IChatView {

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
    @Inject
    ChatPresenter chatPresenter;

    private ChatAdapter chatAdapter;
    private ChatItemDialog chatItemDialog;
    private  LinearLayout.LayoutParams layoutParams;

    boolean isRecording = false;
    private Uri imageUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        ButterKnife.bind(this);
        PeekabooApplication.getApp(this).getComponent().inject(this);
        String receiverName = getIntent().getStringExtra(Constants.EXTRA_RECEIVER_NAME);
        receiverName = "test";

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarChat);
        layoutParams = (LinearLayout.LayoutParams) rflMessageBody.getLayoutParams();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (null != receiverName) {
            getSupportActionBar().setTitle(receiverName);
        }

        chatAdapter = new ChatAdapter(getApplicationContext(), chatPresenter, this);
        chatPresenter.bind(this, receiverName);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rvMessages.setLayoutManager(layoutManager);

        rvMessages.setItemAnimator(new DefaultItemAnimator());
        rvMessages.setAdapter(chatAdapter);
        rvMessages.addOnItemTouchListener(new ChatRecyclerTouchListener(this, rvMessages, new ChatClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                chatItemDialog = new ChatItemDialog();
                Bundle itemIndexBundle = new Bundle();
                itemIndexBundle.putInt(Constants.ARG_CHAT_MESSAGE_ITEM_INDEX, position);
                chatItemDialog.setArguments(itemIndexBundle);
                chatItemDialog.show(ft, Constants.FRAGMENT_TAGS.CHAT_ITEM_DIALOG_FRAGMENT_TAG);
            }
        }));
    }


    @Override
    protected void onResume() {
        super.onResume();
        chatPresenter.onResume();
        chatPresenter.onChatHistoryLoading(chatAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        chatPresenter.onPause();
    }

    @Override
    protected void onDestroy() {
        chatPresenter.unbind();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dialogs_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dialogsDrop: {
                chatPresenter.onDeleteChatHistoryButtonPress(chatAdapter);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.bSendMessage)
    void onButtonSendCLick() {
        chatPresenter.onSendTextButtonPress();
    }

    @OnClick(R.id.photo_btn)
    void onCameraButtonCLick(){
        takePhoto();
    }

    @OnClick(R.id.gallery_btn)
    void onGalleryButtonClick(){
        takeGalleryImage();
    }

    @OnClick(R.id.micro_btn)
    void onRecordButtonClick(){
        takeAudio();
    }


    @OnClick(R.id.bMesageOpen)
    void onbMessageOpenClick(){
        rflMessageBody.setVisibility(View.VISIBLE);
        layoutParams.weight = 6;
        layoutParams.width = 0;
        rflMessageBody.setLayoutParams(layoutParams);
        layoutParams.weight = 3;
        layoutParams.width = 0;
        svItems.setLayoutParams(layoutParams);
        etMessageBody.post(() -> {
            float cx, cy;
            cx = (bMessageOpen.getX() + bMessageOpen.getWidth())/2;
            cy = (bMessageOpen.getY() + bMessageOpen.getHeight())/2;

            float dx = Math.max(cx, flMessageBody.getWidth() - cx);
            float dy = Math.max(cy, flMessageBody.getHeight() - cy);
            float finalRadius = (float) Math.hypot(dx, dy);

            Animator animator =
                    ViewAnimationUtils.createCircularReveal(flMessageBody, (int)cx, (int)cy, 0, finalRadius);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(300);
            animator.start();
        });

        bMessageOpen.setVisibility(View.GONE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODES.REQUEST_CODE_CAMERA) {
            Toast.makeText(getApplicationContext(), "CAMERA: " + resultCode, Toast.LENGTH_SHORT).show();
            if (resultCode == RESULT_OK) {
                sendImage(imageUri);
                Timber.tag("IMAGE_URI").wtf("URI: " + imageUri);
                galleryAddPic(imageUri);
            }
        }
        if (requestCode == Constants.REQUEST_CODES.REQUEST_CODE_GALERY) {
            Toast.makeText(getApplicationContext(), "GALLERY: " + resultCode, Toast.LENGTH_SHORT).show();
            if (resultCode == RESULT_OK && null != data) {
                sendImage(data.getData());
            }
        }
        if (requestCode == Constants.REQUEST_CODES.REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && null != data) {
                chatPresenter.onSendAudioButtonPress(data);
            }
        }
    }

    private void galleryAddPic(Uri imageUri) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(imageUri);
        sendBroadcast(mediaScanIntent);
    }


    public void takeGalleryImage() {
        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                Constants.REQUEST_CODES.REQUEST_CODE_GALERY);
    }

    public void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = Utility.createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                imageUri = Utility.getImageContentUri(ChatActivity.this, photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, Constants.REQUEST_CODES.REQUEST_CODE_CAMERA);
            }
        }
    }

    public void takeAudio() {
        Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
    }

    public void takeDocument() {
        Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
    }

    public void recordAudio() {
        if (!isRecording) {
            chatPresenter.onStartRecordingAudioClick();
            isRecording = true;
        } else {
            chatPresenter.onStopRecordingAudioClick();
            isRecording = false;
        }
    }

    public boolean sendImage(Uri uri) {
        if(uri == null){
            return false;
        }
        chatPresenter.onSendImageButtonPress(uri);
        Utility.galleryAddPic(ChatActivity.this, uri);
        return true;
    }

    @Override
    public void showToastMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void clearTextField() {
        etMessageBody.setText("");
    }

    @Override
    public String getMessageText() {
        return etMessageBody.getText().toString().trim().replaceAll("[\\s&&[^\r?\n]]+", " ");
    }

    @Override
    public void copyText(int index) {
        chatPresenter.onCopyMessageTextClick((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE),
                chatAdapter.getItem(index));
    }

    @Override
    public void deleteMess(int index) {
        chatPresenter.onDeleteMessageClick(chatAdapter.getItem(index));
    }

    @Override
    public void textToSpeech(int index) {
        chatPresenter.onConvertTextToSpeechClick(chatAdapter.getItem(index));
    }

    @Override
    public void toLastMessage(){
        Timber.tag("MESSAGE").wtf("count: " + chatAdapter.getItemCount());
        rvMessages.scrollToPosition(chatAdapter.getItemCount() - 1);
    }

    @OnFocusChange(R.id.etMessageBody)
    public void onEditTextTouched(){

        if(etMessageBody.hasFocus()){
            bSendMessage.setBackgroundResource(R.drawable.paper_plane2);
            layoutParams.weight=10;
        }else {
            bSendMessage.setBackgroundResource(R.drawable.paper_plane);
            layoutParams.weight=4;
        }
        rflMessageBody.setLayoutParams(layoutParams);
    }

}