package com.peekaboo.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.peekaboo.R;
import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.adapters.TestMessageAdapter;
import com.peekaboo.presentation.presenters.ChatPresenter2;
import com.peekaboo.presentation.views.IChatView2;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sebastian on 22.08.16.
 */
public class MessangerTestFragment extends Fragment implements IChatView2 {
    public static final String RECEIVER_ID = "receiverId";
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.sendButton)
    Button sendButton;
    @BindView(R.id.messageView)
    EditText messageView;
    @Inject
    ChatPresenter2 presenter;
    private TestMessageAdapter adapter;
    private boolean firstLaunch = true;
    private String receiverId;

    public static Fragment newInstance(String receiverId) {
        Fragment fragment = new MessangerTestFragment();

        Bundle args = new Bundle();
        args.putString(RECEIVER_ID, receiverId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PeekabooApplication.getApp(getActivity()).getComponent().inject(this);
        receiverId = getArguments().getString(RECEIVER_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_messanger_test, container, false);
        ButterKnife.bind(this, inflate);
        adapter = new TestMessageAdapter(getActivity(), presenter);
        listView.setAdapter(adapter);
        listView.setStackFromBottom(true);
        sendButton.setOnClickListener(v -> {
            String message = messageView.getText().toString();
            if (!message.isEmpty()) {
                presenter.onSendTextButtonPress(message);
            }
        });
        presenter.bind(this);
        return inflate;
    }

    @Override
    public void onDestroyView() {
        presenter.unbind();
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume(firstLaunch, receiverId);
        firstLaunch = false;
    }

    @Override
    public void onPause() {
        presenter.onPause();
        super.onPause();
    }

    @Override
    public void clearTextField() {
        messageView.setText("");
    }

    @Override
    public String getMessageText() {
        return messageView.getText().toString();
    }

    @Override
    public void showMessages(List<PMessage> messages) {
        adapter.setMessages(messages);
    }

    @Override
    public void appendMessages(List<PMessage> messages) {
        Log.e("fragment", "append " + messages);
        adapter.addMessages(messages);
        int position = adapter.getCount() - 1;
        listView.smoothScrollToPosition(position);
        listView.setSelection(position);
    }

    @Override
    public void updateMessage(PMessage message) {
        Log.e("fragment", "update " + message);
        adapter.updateMessage(message);
    }

    @Override
    public String getCompanionId() {
        return receiverId;
    }

    @Override
    public void showRecordStart() {

    }

    @Override
    public void showRecordStop() {

    }

    @Override
    public void showToastMessage(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }
}
