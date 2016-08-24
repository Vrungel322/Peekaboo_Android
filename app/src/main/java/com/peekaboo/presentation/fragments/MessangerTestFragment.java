package com.peekaboo.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.peekaboo.R;
import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.data.repositories.database.messages.PMessageAbs;
import com.peekaboo.data.repositories.database.messages.TextPMessage;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.domain.User;
import com.peekaboo.domain.subscribers.BaseUseCaseSubscriber;
import com.peekaboo.domain.usecase.FindFriendUseCase;
import com.peekaboo.domain.usecase.Messanger;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.adapters.TestMessageAdapter;
import com.peekaboo.presentation.services.INotifier;
import com.peekaboo.utils.Utility;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sebastian on 22.08.16.
 */
public class MessangerTestFragment extends Fragment implements INotifier.NotificationListener<PMessage> {
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.sendButton)
    Button sendButton;
    @BindView(R.id.messageView)
    EditText messageView;
    @BindView(R.id.receiverView)
    EditText receiverView;
    @Inject
    Messanger notifier;
    @Inject
    AccountUser user;
    @Inject
    FindFriendUseCase findFriendUseCase;
    private TestMessageAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PeekabooApplication.getApp(getActivity()).getComponent().inject(this);
        notifier.tryConnect(user.getBearer());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_messanger_test, container, false);
        ButterKnife.bind(this, inflate);
        adapter = new TestMessageAdapter(getActivity());
        listView.setAdapter(adapter);
        listView.setStackFromBottom(true);
        notifier.addListener(this);
        sendButton.setOnClickListener(v -> {
            String message = messageView.getText().toString();
            String receiverName = receiverView.getText().toString();
            if (!message.isEmpty()) {
                findFriendUseCase.setFriendName(receiverName);
                findFriendUseCase.execute(new BaseUseCaseSubscriber<User>() {
                    @Override
                    public void onNext(User receiver) {
                        TextPMessage pMessage = new TextPMessage(Utility.getPackageId(),
                                true, message, System.currentTimeMillis(),
                                PMessageAbs.PMESSAGE_STATUS.STATUS_SENT,
                                receiver.getId(), user.getId());
                        notifier.sendMessage(pMessage);
                    }
                });
            }
        });
        return inflate;
    }

    @Override
    public void onDestroyView() {
        notifier.removeListener(this);
        super.onDestroyView();
    }

    @Override
    public boolean onMessageObtained(PMessage message) {
        Log.e("fragment", "obtained");
        getActivity().runOnUiThread(() -> {
            adapter.addItem(message);
            listView.smoothScrollToPosition(adapter.getCount() - 1);
        });
        return true;
    }

    @Override
    public void onMessageSent(PMessage message) {
        Log.e("fragment", "onMessageSent");
        messageView.setText("");
        adapter.addItem(message);
    }
}
