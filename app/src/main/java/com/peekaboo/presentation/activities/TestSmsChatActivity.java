package com.peekaboo.presentation.activities;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.peekaboo.R;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.presenters.SmsChatPresenter;
import com.peekaboo.presentation.views.ISmsChatView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestSmsChatActivity extends AppCompatActivity implements ISmsChatView{

    @Inject
    SmsChatPresenter presenter;
    @BindView(R.id.message_input_edit_text)
    EditText etMessageInput;
    @BindView(R.id.message_text_view)
    TextView tvMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_sms_chat);
        ButterKnife.bind(this);
        PeekabooApplication.getApp(this).getComponent().inject(this);
        presenter.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume("0689647569");
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @OnClick(R.id.send_message_button)
    public void send(){
        String text = etMessageInput.getText().toString();
        if(!TextUtils.isEmpty(text)){
            presenter.sendMessage(text);
        }

        getHistory();

    }

    private void getHistory(){
        Cursor cur = getContentResolver().query(Uri.parse("content://sms/"), null, null, null, null);

        if (cur.moveToFirst()) { /* false = no sms */
//            do {
                String msgInfo = "";

                for (int i = 0; i < cur.getColumnCount(); i++) {
                    msgInfo += " " + cur.getColumnName(i) + ":" + cur.getString(i);
                }

                Log.wtf("TextSms", "SMS: " + msgInfo);

//                Toast.makeText(this, msgInfo, Toast.LENGTH_SHORT).show();
//            } while (cur.moveToNext());
        }
    }

    @Override
    public void showMessage(String text, String phone) {
        String finalText = tvMessages.getText().toString() + "\n\n" + phone + ": " + text;
        tvMessages.setText(finalText);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showToastMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
