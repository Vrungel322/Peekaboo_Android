package com.peekaboo.presentation.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.peekaboo.R;
import com.peekaboo.data.FileEntity;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.presentation.services.MessageUtils;
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
public class SocketTestFragment extends Fragment implements INotifier.NotificationListener<Message> {
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etMessage)
    EditText etMessage;
    @BindView(R.id.tvResult)
    TextView tvResult;

    @Inject
    FindFriendUseCase findFriendUseCase;
    @Inject
    UploadFileUseCase uploadFileUseCase;
    @Inject
    DownloadFileUseCase downloadFileUseCase;
    @Inject
    INotifier<Message> notifier;
    @Inject
    AccountUser user;

    public SocketTestFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PeekabooApplication.getApp(getActivity()).getComponent().inject(this);
        verifyStoragePermissions(getActivity());
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

//        Message message = MessageUtils.createTextMessage("hello", "afsaghs");
//        if (notifier.isAvailable()) {
//            notifier.sendMessage(message);
//        }
        String friendName = etUsername.getText().toString();
        if (!friendName.isEmpty()) {
            findFriendUseCase.setFriendName(friendName);
            findFriendUseCase.execute(new BaseUseCaseSubscriber<User>() {
                @Override
                public void onNext(final User user) {
                    String message = etMessage.getText().toString();
                    if (message.isEmpty()) {
                        String fileName = "/sdcard/eric.wav";
                        uploadFileUseCase.setInfo(fileName, user.getId());
                        uploadFileUseCase.execute(new BaseUseCaseSubscriber<FileEntity>() {
                            @Override
                            public void onNext(FileEntity fileEntity) {
                                super.onNext(fileEntity);
                                Message typeMessage = MessageUtils.createTypeMessage(user.getId(), Message.Type.AUDIO, fileEntity.getName(), SocketTestFragment.this.user.getId());
                                notifier.sendMessage(typeMessage);
                            }
                        });
                    } else {
                        Message message1 = MessageUtils.createTextMessage(message, user.getId(), SocketTestFragment.this.user.getId());
                        if (notifier.isAvailable()) {
                            notifier.sendMessage(message1);
                        }
                    }

                }
            });
        }
    }

    @OnClick(R.id.bReconnect)
    public void reconnect() {
        Toast.makeText(getActivity(), user.getBearer(), Toast.LENGTH_LONG).show();
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
        String messageType = message.getParams().get(Message.Params.TYPE);
        if (Message.Type.AUDIO.equals(messageType)) {
            String remoteFileName = new String(message.getBody());
            String fileName = "/sdcard/eric10.wav";

            downloadFileUseCase.setInfo(fileName, remoteFileName);
            downloadFileUseCase.execute(new BaseUseCaseSubscriber<File>() {
                @Override
                public void onNext(File file) {
                    super.onNext(file);
                    Toast.makeText(getActivity(), "file has come", Toast.LENGTH_LONG).show();
                    Log.e("file", file.toString());
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                }
            });
        } else if (Message.Type.TEXT.equals(messageType)) {
            tvResult.setText(message.getTextBody());
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, PERMISSIONS_STORAGE[1]);
        int readPermission = ActivityCompat.checkSelfPermission(activity, PERMISSIONS_STORAGE[0]);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    public void onConnected() {

    }

    @Override
    public void onDisconnected() {

    }
}
