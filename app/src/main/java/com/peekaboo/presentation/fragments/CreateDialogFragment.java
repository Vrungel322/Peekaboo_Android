package com.peekaboo.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.gson.Gson;
import com.peekaboo.R;
import com.peekaboo.data.GroupChatCreationEntity;
import com.peekaboo.data.GroupChatMemberEntity;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.services.INotifier;
import com.peekaboo.presentation.services.Message;
import com.peekaboo.presentation.services.MessageUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by arkadii on 10/24/16.
 */

public class CreateDialogFragment extends Fragment implements INotifier.NotificationListener<Message> {

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
        notifier.addListener(this);
        view.findViewById(R.id.bCreate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<GroupChatMemberEntity> members = new ArrayList<>();
                members.add(new GroupChatMemberEntity("476", "Arkadius", "476"));
                members.add(new GroupChatMemberEntity("140", "Oleksii", "476"));
                GroupChatCreationEntity gavnina = new GroupChatCreationEntity(members, "", "gavnina");
                notifier.sendMessage(MessageUtils.groupChatCreateMessage(accountUser.getId(), gavnina));
//                String body = String.format("[%s,%s]", accountUser.getId(), etIds.getText());
//                notifier.sendMessage(new Message(Message.Command.SYSTEMMESSAGE)
//                        .addParam(Message.Params.REASON, Message.Reason.CREATE_DIALOG)
//                        .addParam(Message.Params.FROM, accountUser.getId())
//                        .setBody(body.getBytes()));
            }
        });
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onMessageObtained(Message message) {
        switch (message.getCommand()) {
            case SYSTEMMESSAGE:
                if (Message.Reason.NEW_DIALOG.equals(
                        message.getParams().get(Message.Params.REASON))) {

//                    notifier.sendMessage(new Message(Message.Command.MESSAGE)
//                            .addParam(Message.Params.FROM, accountUser.getId())
//                            .addParam(Message.Params.DESTINATION, message.getParams().get(Message.Params.FROM))
//                            .addParam(Message.Params.TYPE, Message.Type.TEXT)
//                            .setTextBody("бамжур"));

                    List<GroupChatMemberEntity> members = new ArrayList<>();
                    members.add(new GroupChatMemberEntity("140", "Oleksii", "476"));
                    GroupChatCreationEntity gavnina = new GroupChatCreationEntity(members, null, null);
                    String chatId = message.getParams().get(Message.Params.FROM);
                    notifier.sendMessage(new Message(Message.Command.SYSTEMMESSAGE)
                            .addParam(Message.Params.FROM, accountUser.getId())
                            .addParam(Message.Params.DESTINATION, chatId)
                            .addParam(Message.Params.REASON, Message.Reason.REMOVE_USER)
                            .setTextBody(new Gson().toJson(gavnina)));
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        notifier.removeListener(this);
        super.onDestroyView();
    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onDisconnected() {

    }
}
