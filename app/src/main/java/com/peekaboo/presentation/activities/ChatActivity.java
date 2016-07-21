package com.peekaboo.presentation.activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.peekaboo.R;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.adapters.ChatArrayAdapter;
import com.peekaboo.presentation.database.PMessage;
import com.peekaboo.presentation.fragments.AttachmentChatDialog;
import com.peekaboo.presentation.fragments.ChatItemDialog;
import com.peekaboo.presentation.presenters.ChatPresenter;
import com.peekaboo.presentation.utils.ChatMessage;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemLongClick;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * Created by Nataliia on 13.07.2016.
 */
public class ChatActivity extends AppCompatActivity implements ChatItemDialog.IChatItemEventListener {

    @BindView(R.id.etMessageBody)
    EditText etMessageBody;
    @BindView(R.id.lvMessages)
    ListView lvMessages;
    @Inject
    ChatPresenter chatPresenter;

    private boolean side = true;
    private ChatArrayAdapter chatArrayAdapter;
    private AttachmentChatDialog attachmentChatDialog;
    private ChatItemDialog chatItemDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        ButterKnife.bind(this);
        PeekabooApplication.getApp(this).getComponent().inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarChat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.list_item_chat_message_right);
        lvMessages.setAdapter(chatArrayAdapter);
        OverScrollDecoratorHelper.setUpOverScroll(lvMessages);
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
                chatPresenter.dropTableAndCreate("test");
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
        String msgBody = etMessageBody.getText().toString();
        chatArrayAdapter.add(new ChatMessage(side, msgBody, null));
        chatArrayAdapter.notifyDataSetChanged();
        etMessageBody.setText("");
        //TODO: actually sending
        //DB testing
        chatPresenter.createTable("test"); // should be done when friend add
            chatPresenter.makeNoteInTable(new PMessage("idPack", true, msgBody,
                    System.currentTimeMillis(), true, false, false), "test");
        chatPresenter.getTableAsString("test");
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
        chatArrayAdapter.add(new ChatMessage(side, "", photo));
    }
    @OnItemLongClick(R.id.lvMessages)
    boolean onItemLongClick(int position) {
        Log.e("TAG", chatArrayAdapter.getItem(position).message);

        Bundle bundle = new Bundle();
        bundle.putInt("index", position);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        chatItemDialog = new ChatItemDialog();
        chatItemDialog.setArguments(bundle);
        chatItemDialog.show(ft, "chatItemDialog");
        return true;
    }

    @Override
    public void copyText(int index){
        chatArrayAdapter.copyText(index);
    }

    @Override
    public void deleteMess(int index){
        chatArrayAdapter.deleteMess(index);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
