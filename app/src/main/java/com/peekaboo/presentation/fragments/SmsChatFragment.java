package com.peekaboo.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.peekaboo.R;
import com.peekaboo.domain.Sms;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.adapters.SmsChatAdapter;
import com.peekaboo.presentation.pojo.PhoneContactPOJO;
import com.peekaboo.presentation.presenters.SmsChatPresenter;
import com.peekaboo.presentation.views.ISmsChatView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.peekaboo.utils.Constants.EXTRA_RECEIVER_CONTACT;

/**
 * Created by st1ch on 19.10.2016.
 */

public class SmsChatFragment extends Fragment implements ISmsChatView {

    @BindView(R.id.message_body_edit_text)
    EditText etMessageBody;
    @BindView(R.id.sms_messages_recycler_view)
    RecyclerView rvMessages;

    @Inject
    SmsChatPresenter presenter;

//    private String receiverPhone;
    private PhoneContactPOJO phoneContact;
    private SmsChatAdapter adapter;

    public static SmsChatFragment newInstance(PhoneContactPOJO companion) {

        SmsChatFragment fragment = new SmsChatFragment();

        Bundle args = new Bundle();
        args.putParcelable(EXTRA_RECEIVER_CONTACT, companion);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PeekabooApplication.getApp(getActivity()).getComponent().inject(this);
        presenter.bind(this);
        phoneContact = getArguments().getParcelable(EXTRA_RECEIVER_CONTACT);
        presenter.onCreate(phoneContact.getPhone());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(supportActionBar != null){
            supportActionBar.setTitle(phoneContact.getName());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sms_chat, container, false);
        ButterKnife.bind(this, view);

        adapter = new SmsChatAdapter(rvMessages, getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rvMessages.setLayoutManager(layoutManager);
        rvMessages.setItemAnimator(new DefaultItemAnimator());
        rvMessages.setAdapter(adapter);

        return view;
    }

    @OnClick(R.id.bSendSmsMessage)
    public void send(){
        presenter.sendMessage(getMessageText());
    }

    private String getMessageText(){
        return etMessageBody.getText().toString().trim().replaceAll("[\\s&&[^\r?\n]]+", " ");
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        presenter.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void showMessages(List<Sms> messages) {
        adapter.setItems(messages);
    }

    @Override
    public void appendMessage(Sms message) {
        adapter.appendItem(message);
    }

    @Override
    public void clearTextField() {
        etMessageBody.setText("");
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showToastMessage(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }
}
