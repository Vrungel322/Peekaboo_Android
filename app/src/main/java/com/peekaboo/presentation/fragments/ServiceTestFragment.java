package com.peekaboo.presentation.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.peekaboo.R;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.services.INotifier;
import com.peekaboo.presentation.services.Message;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class ServiceTestFragment extends Fragment implements INotifier.NotificationListener {

    @Inject
    INotifier notifier;
    private EditText message;
    private TextView messages;
    private EditText receiver;

    public ServiceTestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PeekabooApplication.getApp(getActivity()).getComponent().inject(this);
        notifier.tryConnect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_service_test, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        notifier.addListener(this);
        message = (EditText) getActivity().findViewById(R.id.message);
        messages = (TextView) getActivity().findViewById(R.id.messages);
        receiver = (EditText) getActivity().findViewById(R.id.receiver);
        getActivity().findViewById(R.id.send_button).setOnClickListener(v -> {
            Log.e("available", String.valueOf(notifier.isAvailable()));
            if (notifier.isAvailable()) {
                notifier.sendMessage(message.getText().toString(), receiver.getText().toString());
            }
        });
    }

    @Override
    public void onDestroyView() {
        notifier.removeListener(this);
        super.onDestroyView();
    }

    @Override
    public void onMessageObtained(Message message) {
        messages.setText(messages.getText() + "\n" + message.getPayload());
    }
}
