package com.peekaboo.presentation.activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.peekaboo.R;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.adapters.ChatAdapter;
import com.peekaboo.data.repositories.database.PMessage;
import com.peekaboo.presentation.fragments.AttachmentChatDialog;
import com.peekaboo.presentation.presenters.ChatPresenter;
import com.peekaboo.utils.Constants;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Nataliia on 13.07.2016.
 */
public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.etMessageBody)
    EditText etMessageBody;
    @BindView(R.id.lvMessages)
    ListView lvMessages;

    @Inject
    ChatPresenter chatPresenter;

    private ChatAdapter chatAdapter;
    private AttachmentChatDialog attachmentChatDialog;
    private CompositeSubscription subscriptions;
    private String receiverName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        ButterKnife.bind(this);
        PeekabooApplication.getApp(this).getComponent().inject(this);
        receiverName = getIntent().getStringExtra(Constants.EXTRA_RECEIVER_NAME);
        receiverName = "test";


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarChat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(null != receiverName){
            toolbar.setTitle(receiverName);
        }

        chatAdapter = new ChatAdapter(getApplicationContext());
        lvMessages.setAdapter(chatAdapter);
        lvMessages.setStackFromBottom(true);
        lvMessages.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        OverScrollDecoratorHelper.setUpOverScroll(lvMessages);

        chatPresenter.createTable(receiverName);

    }

    @Override
    protected void onResume() {
        super.onResume();
        subscriptions = new CompositeSubscription();
        subscriptions.add(chatPresenter.getAllMessages(receiverName, chatAdapter));
        subscriptions.add(chatPresenter.getTableAsString(receiverName));
    }

    @Override
    protected void onPause() {
        super.onPause();
        subscriptions.unsubscribe();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dialogs_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.dialogsDrop:{
                chatPresenter.dropTableAndCreate(receiverName);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.bSendMessage)
    void onButtonSendCLick(){
        sendChatMessage();
    }

    @OnClick(R.id.attach_btn)
    void onButtonAttachClick(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        attachmentChatDialog = new AttachmentChatDialog();
        attachmentChatDialog.show(ft, "attachmentDialog");
    }

    private boolean sendChatMessage() {
        String msgBody = etMessageBody.getText().toString().trim().replaceAll("[\\s&&[^\r?\n]]+", " ");
        if(null == msgBody || msgBody.equals("")){
            return false;
        }
        chatPresenter.makeNoteInTable(receiverName, new PMessage("pckgId", true, msgBody,
                System.currentTimeMillis(), false, false, false));
        etMessageBody.setText("");
        //TODO: actually sending

        //DB testing
        chatPresenter.getTableAsString(receiverName);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AttachmentChatDialog.REQUEST_CODE_CAMERA) {
            Toast.makeText(getApplicationContext(), "" + resultCode, Toast.LENGTH_SHORT).show();
            Bitmap thumbnailBitmap = (Bitmap) data.getExtras().get("data");
            sendPhoto(thumbnailBitmap);
        }
        if (requestCode == AttachmentChatDialog.REQUEST_CODE_GALERY){
            Toast.makeText(getApplicationContext(), "" + resultCode, Toast.LENGTH_SHORT).show();
            Bitmap thumbnailBitmap = null;
            try {
                thumbnailBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            sendPhoto(thumbnailBitmap);
        }
    }

    private void sendPhoto(Bitmap photo){
        chatPresenter.makeNoteInTable(receiverName, new PMessage("photoPckgId", true, "PHOTO",
                System.currentTimeMillis(), false, false, false));
    }
}