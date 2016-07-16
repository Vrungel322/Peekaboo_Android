package com.peekaboo.presentation.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.peekaboo.R;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.services.INotifier;
import com.peekaboo.presentation.services.Message;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

//    @Inject
//    INotifier notifier;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        PeekabooApplication.getApp(this).getComponent().inject(this);
//        if (!notifier.isAvailable()) {
//            notifier.tryConnect();
//        }
//        editText = (EditText) findViewById(R.id.message);
//        findViewById(R.id.sendButton).setOnClickListener(view -> {
//            boolean available = notifier.isAvailable();
//            Log.e("main activity", String.valueOf(available));
//            if (available) {
//                notifier.sendMessage(new Message(editText.getText().toString()));
//            }
//        });
    }


}
