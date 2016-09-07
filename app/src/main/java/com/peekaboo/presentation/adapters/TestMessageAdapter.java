package com.peekaboo.presentation.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.peekaboo.R;
import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.data.repositories.database.messages.PMessageAbs;
import com.peekaboo.presentation.presenters.ChatPresenter2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sebastian on 22.08.16.
 */
public class TestMessageAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private final List<PMessage> messages = new ArrayList<>();
    Handler handler;
    private ChatPresenter2 presenter;

    public TestMessageAdapter(Context context, ChatPresenter2 presenter) {
        this.presenter = presenter;
        inflater = LayoutInflater.from(context);
        handler = new Handler();
    }

    public void addItem(PMessage item) {
        messages.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public PMessage getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PMessage item = getItem(position);

        TextView view = (TextView) inflater.inflate(R.layout.test_list_item, parent, false);
        int status = item.status();
        String statusStr = status == PMessageAbs.PMESSAGE_STATUS.STATUS_SENT ?
                "sent"
                : status == PMessageAbs.PMESSAGE_STATUS.STATUS_DELIVERED ?
                "delivered"
                : "read";
        switch (item.mediaType()) {
            case PMessageAbs.PMESSAGE_MEDIA_TYPE.TEXT_MESSAGE:
                showTextMessage(item, view, statusStr);
                break;
            case PMessageAbs.PMESSAGE_MEDIA_TYPE.AUDIO_MESSAGE:
                showAudioMessage(item, view, statusStr);
                break;
        }


        boolean isDelivered = status == PMessageAbs.PMESSAGE_STATUS.STATUS_DELIVERED && !item.isMine();

        view.setBackgroundColor(isDelivered ? Color.GRAY : Color.WHITE);

        if (isDelivered) {
            handler.postDelayed(() -> presenter.onUserMessageRead(item), 500);
        }

        return view;
    }

    private void showAudioMessage(PMessage item, TextView view, String statusStr) {
        view.setText(statusStr + " " + item.messageBody());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void showTextMessage(PMessage item, TextView view, String statusStr) {
        view.setText(statusStr + " " + item.messageBody());
        view.setGravity(item.isMine() ? Gravity.RIGHT : Gravity.LEFT);
    }

    public void setMessages(List<PMessage> messages) {
        this.messages.clear();
        this.messages.addAll(messages);
        notifyDataSetChanged();
    }

    public void addMessages(List<PMessage> messages) {
        this.messages.addAll(messages);
        notifyDataSetChanged();
    }

    public void updateMessage(PMessage message) {
        int size = messages.size();
        if (size == 0 || messages.get(size - 1).id() < message.id()) {
            ArrayList<PMessage> messages = new ArrayList<>();
            messages.add(message);
            addMessages(messages);
        } else {
            for (int i = size - 1; i >= 0; i--) {
                PMessage pMessage = messages.get(i);
                if (pMessage.equals(message)) {
                    messages.set(i, message);
                    notifyDataSetChanged();
                    break;
                }
            }
        }
    }
}