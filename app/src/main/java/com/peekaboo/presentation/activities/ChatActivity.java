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
    public static String sTESTdbName = "test";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        ButterKnife.bind(this);
        PeekabooApplication.getApp(this).getComponent().inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarChat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.list_item_chat_message);
        chatPresenter.createTable(sTESTdbName); // should be done when friend add
        chatArrayAdapter.setPreviousMessages(chatPresenter.getAllNotes(sTESTdbName));
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
            case R.id.itClaerDialog:{
                chatArrayAdapter.getChatMessageList().clear();
                chatArrayAdapter.notifyDataSetChanged();
                chatPresenter.dropTableAndCreate(sTESTdbName);
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
        chatArrayAdapter.add(new PMessage("packageId", true, msgBody, System.currentTimeMillis(),
                true, true, true));
        chatArrayAdapter.notifyDataSetChanged();
        etMessageBody.setText("");
        //TODO: actually sending
        //DB testing
            chatPresenter.makeNoteInTable(new PMessage("idPack", true, msgBody,
                    System.currentTimeMillis(), true, true, true), sTESTdbName);
        chatPresenter.getTableAsString(sTESTdbName);
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
        chatArrayAdapter.add(new PMessage("photoId", true, "", System.currentTimeMillis(),
                true, false, false));
    }
    @OnItemLongClick(R.id.lvMessages)
    boolean onItemLongClick(int position) {
        Log.e("TAG", chatArrayAdapter.getItem(position).getMessageBody());

        Bundle bundle = new Bundle();
        bundle.putInt("index", position);
        bundle.putString("msgBody", chatArrayAdapter.getItem(position).getMessageBody());
        bundle.putString("tableName", sTESTdbName);
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
    public void deleteMess(String tableName, int index, String msgBody){
        chatArrayAdapter.deleteMess(index);
        chatPresenter.deleteCortegeFromDB(tableName, index, msgBody);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
