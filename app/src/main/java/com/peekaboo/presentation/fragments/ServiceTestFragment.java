package com.peekaboo.presentation.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.peekaboo.R;
import com.peekaboo.data.rest.PeekabooApi;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.domain.AudioRecorder;
import com.peekaboo.domain.Record;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.services.INotifier;
import com.peekaboo.presentation.services.Message;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

public class ServiceTestFragment extends Fragment implements INotifier.NotificationListener {

    @Inject
    INotifier notifier;
    @Inject
    AccountUser user;
    @Inject
    PeekabooApi api;
    private EditText message;
    private TextView messages;
    private EditText receiver;
    private Button recordButton;
    private AudioRecorder audioRecorder;
    private Record record;
    private OkHttpClient client;

    public ServiceTestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PeekabooApplication.getApp(getActivity()).getComponent().inject(this);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder()
                .readTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();
        notifier.tryConnect(user.getBearer());
    }

    private void sendFile(String fileName) {
//        String fileName = Environment.getExternalStorageDirectory().toString() + File.separator + "eric.wav";
        File file = new File(fileName);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("task_file", "file",
                        RequestBody.create(MediaType.parse("audio/flac"), file))
                .build();
        Request request = new Request.Builder()
                .url("http://text-to-speech-java-oleksiiromanko.mybluemix.net/UploadServlet")
//                .url("http://10.0.1.5:8080/UploadServlet")
                .addHeader("Level", "0.3")
                .post(requestBody)
                .build();
//
//
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("error", String.valueOf(e));
                messages.post(() -> messages.setText(String.valueOf(e)));
            }
//
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("response", String.valueOf(response));

                String string = response.body().string();
                Log.e("response", String.valueOf(Thread.currentThread()));
                messages.post(() -> messages.setText(Html.fromHtml(string)));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_service_test, container, false);
    }

    boolean isRecording;
    Subscription subscription = Subscriptions.empty();
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        notifier.addListener(this);
        message = (EditText) getActivity().findViewById(R.id.message);
        messages = (TextView) getActivity().findViewById(R.id.messages);
        messages.append("");
        receiver = (EditText) getActivity().findViewById(R.id.receiver);
        recordButton = (Button) getActivity().findViewById(R.id.record_button);
        record = new Record("test1");
        audioRecorder = new AudioRecorder(record);

        recordButton.setOnClickListener(v -> {
            if (!isRecording) {
                subscription = audioRecorder.startRecording().subscribe();
                recordButton.setText("Stop Record");
                messages.setText("");
            } else {
                subscription = audioRecorder.stopRecording().subscribe(record1 -> {
                    sendFile(record1.getFilename());
                });
                recordButton.setText("Start Record");
                messages.setText("Sending...");
            }
            isRecording = !isRecording;
        });
        getActivity().findViewById(R.id.reconnect_button).setOnClickListener(v -> {
            Log.e("available", String.valueOf(notifier.isAvailable()));
            notifier.tryConnect(user.getBearer());
        });
//
//        getActivity().findViewById(R.id.send_button).setOnClickListener(v -> {
//            Log.e("available", String.valueOf(notifier.isAvailable()));
//            if (notifier.isAvailable()) {
//                String receiver = this.receiver.getText().toString();
//                api.getFriend(receiver).enqueue(new retrofit2.Callback<UserEntity>() {
//                    @Override
//                    public void onResponse(retrofit2.Call<UserEntity> call, retrofit2.Response<UserEntity> response) {
//                        String fileName = Environment.getExternalStorageDirectory().toString() + File.separator + "eric.wav";
//
//                        Message audioMessage = MessageUtils.createTypeMessage(response.body().getId(), Message.Type.AUDIO);
//                        notifier.sendFile(audioMessage, fileName);
//                    }
//
//                    @Override
//                    public void onFailure(retrofit2.Call<UserEntity> call, Throwable t) {
//                        Log.e("failure", String.valueOf(t));
//                    }
//                });
//            }
//        });

        getActivity().findViewById(R.id.disconnect_button).setOnClickListener(v -> notifier.disconnect());
    }

    @Override
    public void onDestroyView() {
        notifier.removeListener(this);
        super.onDestroyView();
    }

    @Override
    public void onMessageObtained(Message message) {
        Log.e("thread", String.valueOf(Thread.currentThread()));
        try {
            messages.append(String.format("%s", new String(message.getBody(), "UTF-8")));
        } catch (UnsupportedEncodingException e) {
            Log.e("exception", String.valueOf(e));
        }
    }
}
