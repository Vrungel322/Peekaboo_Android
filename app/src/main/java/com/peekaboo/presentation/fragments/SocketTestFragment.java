package com.peekaboo.presentation.fragments;


import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.peekaboo.R;
import com.peekaboo.data.FileEntity;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.domain.MessageUtils;
import com.peekaboo.domain.User;
import com.peekaboo.domain.subscribers.BaseUseCaseSubscriber;
import com.peekaboo.domain.usecase.DownloadFileUseCase;
import com.peekaboo.domain.usecase.FindFriendUseCase;
import com.peekaboo.domain.usecase.UploadFileUseCase;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.services.INotifier;
import com.peekaboo.presentation.services.Message;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SocketTestFragment extends Fragment implements INotifier.NotificationListener {
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.tvResult)
    TextView tvResult;
    @Inject
    FindFriendUseCase findFriendUseCase;
    @Inject
    UploadFileUseCase uploadFileUseCase;
    @Inject
    DownloadFileUseCase downloadFileUseCase;
    @Inject
    INotifier notifier;
    @Inject
    AccountUser user;

    public SocketTestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PeekabooApplication.getApp(getActivity()).getComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_socket_test, container, false);
        ButterKnife.bind(this, inflate);
        notifier.addListener(this);
        return inflate;
    }

    @Override
    public void onDestroyView() {
        notifier.removeListener(this);
        super.onDestroyView();
    }

    @OnClick(R.id.bSend)
    public void send() {
        String friendName = etUsername.getText().toString();
        findFriendUseCase.setFriendName(friendName);
        findFriendUseCase.execute(new BaseUseCaseSubscriber<User>() {
            @Override
            public void onNext(User user) {
                String fileName = Environment.getExternalStorageDirectory().toString() + File.separator + "eric.wav";
                uploadFileUseCase.setInfo(fileName, user.getId());
                uploadFileUseCase.execute(new BaseUseCaseSubscriber<FileEntity>() {
                    @Override
                    public void onNext(FileEntity fileEntity) {
                        super.onNext(fileEntity);
                        Message typeMessage = MessageUtils.createTypeMessage(user.getId(), Message.Type.AUDIO, fileEntity.getName());
                        notifier.sendMessage(typeMessage);
                    }
                });
            }
        });
    }

    @OnClick(R.id.bReconnect)
    public void reconnect() {
        if (!notifier.isAvailable()) {
            notifier.tryConnect(user.getBearer());
        }
    }

    @OnClick(R.id.bDisconnect)
    public void disconnect() {
        notifier.disconnect();
    }

    @Override
    public void onMessageObtained(Message message) {
        Log.e("message", message.toString());
        String remoteFileName = new String(message.getBody());
        String fileName = Environment.getExternalStorageDirectory().toString() + File.separator + "eric10.wav";

        downloadFileUseCase.setInfo(fileName, remoteFileName);
        downloadFileUseCase.execute(new BaseUseCaseSubscriber<File>() {
            @Override
            public void onNext(File file) {
                super.onNext(file);
                Log.e("file", file.toString());
            }
        });
    }
}
