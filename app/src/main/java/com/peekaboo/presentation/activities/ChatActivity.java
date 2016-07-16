package com.peekaboo.presentation.activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.peekaboo.R;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.adapters.ChatArrayAdapter;
import com.peekaboo.presentation.fragments.AttachmentChatDialog;
import com.peekaboo.presentation.presenters.ChatPresenter;
import com.peekaboo.presentation.utils.ChatMessage;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Nataliia on 13.07.2016.
 */
public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.attach_btn)
    Button buttonAttach;
    @BindView(R.id.send_message_btn)
    Button buttonSend;
    @BindView(R.id.edit_text_msg)
    EditText messageText;
    @BindView(R.id.msg_view)
    ListView messageList;
    @Inject
    ChatPresenter chatPresenter;

    private boolean side = true;
    public ChatArrayAdapter chatArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        ButterKnife.bind(this);
        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.list_item_chat_message_right);
        messageList.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        messageList.setAdapter(chatArrayAdapter);
//        Log.e("actionBar", String.valueOf(getSupportActionBar()));
        PeekabooApplication.getApp(this).getComponent().inject(this);

        sendOnKey();

        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                messageList.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });


//        chatPresenter.bind(this);
//        bus.register(this);
    }
    @OnClick(R.id.send_message_btn)
    void onButtonSendCLick(){
        sendChatMessage();
    }

    @OnClick(R.id.attach_btn)
    void onButtonAttachClick(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        AttachmentChatDialog attachmentChatDialog = new AttachmentChatDialog();
        attachmentChatDialog.show(ft, "attachmentDialog");
    }

    public void sendOnKey(){
        messageText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
    }
    private boolean sendChatMessage() {
        chatArrayAdapter.add(new ChatMessage(side, messageText.getText().toString(), null));
        messageText.setText("");
        //TODO: actually sending
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            Bitmap thumbnailBitmap = (Bitmap) data.getExtras().get("data");
            sendPhoto(thumbnailBitmap);
        }
    }

    private void sendPhoto(Bitmap photo){
        chatArrayAdapter.add(new ChatMessage(side, "", photo));
    }
}
