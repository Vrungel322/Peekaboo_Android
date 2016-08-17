package com.peekaboo.presentation.activities;

import android.app.FragmentTransaction;
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
import android.widget.EditText;
import android.widget.Toast;

import com.peekaboo.R;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.adapters.ChatAdapter;
import com.peekaboo.presentation.fragments.AttachmentChatDialog;
import com.peekaboo.presentation.listeners.ChatOnClickListener;
import com.peekaboo.presentation.listeners.ChatRecyclerTouchListener;
import com.peekaboo.presentation.presenters.ChatPresenter;
import com.peekaboo.presentation.views.IChatView;
import com.peekaboo.utils.Constants;
import com.peekaboo.utils.Utility;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Nataliia on 13.07.2016.
 */
public class ChatActivity extends AppCompatActivity
        implements AttachmentChatDialog.IAttachmentDialogEventListener, IChatView {

    @BindView(R.id.etMessageBody)
    EditText etMessageBody;
    @BindView(R.id.rvMessages)
    RecyclerView rvMessages;
    @Inject
    ChatPresenter chatPresenter;

    private ChatAdapter chatAdapter;

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
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (null != receiverName) {
            getSupportActionBar().setTitle(receiverName);
        }

        chatAdapter = new ChatAdapter(getApplicationContext(), chatPresenter);
        chatPresenter.bind(this, receiverName);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rvMessages.setLayoutManager(layoutManager);
        rvMessages.setItemAnimator(new DefaultItemAnimator());
        rvMessages.setAdapter(chatAdapter);
        rvMessages.addOnItemTouchListener(new ChatRecyclerTouchListener(this, rvMessages,
                                                                        new ChatOnClickListener(ChatActivity.this)));

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

    @OnClick(R.id.attach_btn)
    void onButtonAttachClick() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        AttachmentChatDialog attachmentChatDialog = new AttachmentChatDialog();
        attachmentChatDialog.show(ft, Constants.FRAGMENT_TAGS.ATTACHMENT_DIALOG_FRAGMENT_TAG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODES.REQUEST_CODE_CAMERA) {
            Toast.makeText(getApplicationContext(), "CAMERA: " + resultCode, Toast.LENGTH_SHORT).show();
            if (resultCode == RESULT_OK) {
                sendImage(imageUri);
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

    @Override
    public void takeGalleryImage() {
        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                Constants.REQUEST_CODES.REQUEST_CODE_GALERY);
    }

    @Override
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

    @Override
    public void takeAudio() {
        Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void takeDocument() {
        Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
    }

    @Override
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
}