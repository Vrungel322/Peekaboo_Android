package com.peekaboo.presentation.activities;

import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.peekaboo.R;
import com.peekaboo.data.repositories.database.messages.AudioPMessage;
import com.peekaboo.data.repositories.database.messages.ImagePMessage;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Nataliia on 13.07.2016.
 */
public class ChatActivity extends AppCompatActivity implements ChatItemDialog.IChatItemEventListener,
        AttachmentChatDialog.IAttachmentDialogEventListener, IChatView {

    @BindView(R.id.etMessageBody)
    EditText etMessageBody;
    @BindView(R.id.rvMessages)
    RecyclerView rvMessages;
    @Inject
    ChatPresenter chatPresenter;

    private ChatAdapter chatAdapter;
    private AttachmentChatDialog attachmentChatDialog;
    private ChatItemDialog chatItemDialog;
    private CompositeSubscription subscriptions;
    private String receiverName;

    boolean isRecording = false;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        ButterKnife.bind(this);
        PeekabooApplication.getApp(this).getComponent().inject(this);
        chatPresenter.bind(this);
        receiverName = getIntent().getStringExtra(Constants.EXTRA_RECEIVER_NAME);
        receiverName = "test";


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarChat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (null != receiverName) {
            getSupportActionBar().setTitle(receiverName);
        }

        chatAdapter = new ChatAdapter(getApplicationContext(), chatPresenter);
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

        chatPresenter.createTable(receiverName);

    }

    @Override
    protected void onResume() {
        super.onResume();
        chatPresenter.onResume();
        subscriptions = new CompositeSubscription();
        subscriptions.add(chatPresenter.getAllMessages(receiverName, chatAdapter));
        subscriptions.add(chatPresenter.getUnreadMessagesCount(receiverName));
    }

    @Override
    protected void onPause() {
        super.onPause();
        chatPresenter.onPause();
        subscriptions.unsubscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chatPresenter.unbind();
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
                chatPresenter.dropTableAndCreate(receiverName);
                chatAdapter.clearList();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.bSendMessage)
    void onButtonSendCLick() {
        sendChatMessage();
    }

    @OnClick(R.id.attach_btn)
    void onButtonAttachClick() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        attachmentChatDialog = new AttachmentChatDialog();
        attachmentChatDialog.show(ft, "attachmentDialog");
    }

    private boolean sendChatMessage() {
        String msgBody = etMessageBody.getText().toString().trim().replaceAll("[\\s&&[^\r?\n]]+", " ");
        if (null == msgBody || msgBody.equals("")) {
            return false;
        }
        String pckgId = Utility.getPackageId();
        long timestamp = System.currentTimeMillis();
        // for test
        Random random = new Random();
        boolean isMine = random.nextBoolean();
        chatPresenter.insertMessageToTable(receiverName, new TextPMessage(pckgId, isMine, msgBody,
                timestamp, false, false, false));
        etMessageBody.setText("");
        //TODO: actually sending

        //DB testing
//        chatPresenter.getTableAsString(receiverName);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODES.REQUEST_CODE_CAMERA) {
            Toast.makeText(getApplicationContext(), "CAMERA: " + resultCode, Toast.LENGTH_SHORT).show();
            if (resultCode == RESULT_OK) {
                sendPhoto(imageUri);
                galleryAddPic(imageUri);
            }
        }
        if (requestCode == Constants.REQUEST_CODES.REQUEST_CODE_GALERY) {
            Toast.makeText(getApplicationContext(), "GALLERY: " + resultCode, Toast.LENGTH_SHORT).show();
            if (resultCode == RESULT_OK) {
                sendPhoto(data.getData());
            }
        }
        if (requestCode == Constants.REQUEST_CODES.REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String pckgId = Utility.getPackageId();
                long timestamp = System.currentTimeMillis();
                chatPresenter.insertMessageToTable(receiverName, new AudioPMessage(pckgId, true,
                        result.get(0), timestamp, false, false, false));
            }
        }
    }

    private void sendPhoto(Uri uri) {
        chatPresenter.insertMessageToTable(receiverName, new ImagePMessage(Utility.getPackageId(), true,
                uri.toString(), System.currentTimeMillis(), false, false, false));
    }

    @Override
    public void copyText(int index) {
        chatPresenter.copyMessageText(chatAdapter.getItem(index));
    }

    @Override
    public void deleteMess(int index) {
        chatPresenter.deleteMessageByPackageId(receiverName, chatAdapter.getItem(index));
    }

    @Override
    public void textToSpeech(int index) {
        chatPresenter.convertTextToSpeech(chatAdapter.getItem(index));
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
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                imageUri = getImageContentUri(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, Constants.REQUEST_CODES.REQUEST_CODE_CAMERA);
            }
        }
    }

    public Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        return image;
    }

    private void galleryAddPic(Uri imageUri) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(imageUri);
        sendBroadcast(mediaScanIntent);
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
            chatPresenter.startRecordingAudio(receiverName, 16000);
            isRecording = true;
        } else {
            chatPresenter.stopRecordingAudio(receiverName);
            isRecording = false;
        }
    }

    @Override
    public void onError(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}