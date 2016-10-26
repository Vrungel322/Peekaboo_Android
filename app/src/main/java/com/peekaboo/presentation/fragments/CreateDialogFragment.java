package com.peekaboo.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.peekaboo.R;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.services.INotifier;
import com.peekaboo.presentation.services.Message;

import javax.inject.Inject;

/**
 * Created by arkadii on 10/24/16.
 */

public class CreateDialogFragment extends Fragment {

    public static final String USER_ID = "user_id";
    private EditText etIds;
    @Inject
    INotifier<Message> notifier;
    @Inject
    AccountUser accountUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((PeekabooApplication) getActivity().getApplicationContext()).getComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_dialog, container);
        etIds = (EditText) view.findViewById(R.id.etIds);
        view.findViewById(R.id.bCreate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String body = String.format("[%s,%s]", accountUser.getId(), etIds.getText());
                notifier.sendMessage(new Message(Message.Command.SYSTEMMESSAGE)
                        .addParam(Message.Params.REASON, Message.Reason.CREATE_DIALOG)
                        .addParam(Message.Params.FROM, accountUser.getId())
                        .setBody(body.getBytes()));
            }
        });
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
