package com.peekaboo.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.peekaboo.R;
import com.peekaboo.presentation.utils.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

///**
// * Created by Nataliia on 13.07.2016.
// */
public class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {

    private TextView chatText;
    private TextView timeStamp;
    private List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
    private Context context;

    @Override
    public void add(ChatMessage object) {
        chatMessageList.add(object);
        super.add(object);
    }

    public ChatArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    public int getCount() {
        return this.chatMessageList.size();
    }

    public ChatMessage getItem(int index) {
        return this.chatMessageList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage chatMessageObj = getItem(position);
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = chatMessageObj.left ? inflater.inflate(R.layout.list_item_chat_message_right, parent, false) : inflater.inflate(R.layout.list_item_chat_message_left, parent, false);
        chatText = (TextView) row.findViewById(R.id.chat_message_text_view);
        chatText.setText(chatMessageObj.message);
        timeStamp = (TextView) row.findViewById(R.id.chat_timestamp);
        timeStamp.setText(getTime());
        return row;
    }
    public String getTime(){
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
        return sdf.format(date);
    }
}








