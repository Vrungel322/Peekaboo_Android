package com.peekaboo.presentation.activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

//    @BindView(R.id.attach_btn)
//    Button buttonAttach;
//    @BindView(R.id.bSendMessage)
//    Button buttonSend;
    @BindView(R.id.etMessageBody)
    EditText etMessageBody;
    @BindView(R.id.lvMessages)
    ListView lvMessages;
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
//        lvMessages.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lvMessages.setAdapter(chatArrayAdapter);
//        Log.e("actionBar", String.valueOf(getSupportActionBar()));
        PeekabooApplication.getApp(this).getComponent().inject(this);

//        sendOnKey();

        // зачем етот метод если есть chatArrayAdapter.notifyDataSetChanged(); (97) ???
//        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
//            @Override
//            public void onChanged() {
//                super.onChanged();
//                lvMessages.setSelection(chatArrayAdapter.getCount() - 1);
//                Toast.makeText(getApplicationContext(), "onChange", Toast.LENGTH_SHORT).show();
//            }
//        });


//        chatPresenter.bind(this);
//        bus.register(this);
    }
    @OnClick(R.id.bSendMessage)
    void onButtonSendCLick(){
        sendChatMessage();
    }

    @OnClick(R.id.attach_btn)
    void onButtonAttachClick(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        AttachmentChatDialog attachmentChatDialog = new AttachmentChatDialog();
        attachmentChatDialog.show(ft, "attachmentDialog");
    }

//    public void sendOnKey(){
//        etMessageBody.setOnKeyListener(new View.OnKeyListener() {
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
//                    return sendChatMessage();
//                }
//                return false;
//            }
//        });
//    }

    private boolean sendChatMessage() {
        chatArrayAdapter.add(new ChatMessage(side, etMessageBody.getText().toString()));
        chatArrayAdapter.notifyDataSetChanged();
        etMessageBody.setText("");
        //TODO: actually sending
        return true;
    }

}
