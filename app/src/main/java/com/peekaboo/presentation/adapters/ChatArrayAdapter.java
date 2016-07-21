package com.peekaboo.presentation.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.peekaboo.R;
import com.peekaboo.presentation.utils.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

///**
// * Created by Nataliia on 13.07.2016.
// */
public class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {

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

    public View getView(int position, View view, ViewGroup parent) {
        ChatMessage chatMessageObj = getItem(position);
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = chatMessageObj.right
                    ? inflater.inflate(R.layout.list_item_chat_message_right, parent, false)
                    : inflater.inflate(R.layout.list_item_chat_message_left, parent, false);

            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        holder.tvChatMessage.setText(chatMessageObj.message);
        holder.ivChatImage.setImageBitmap(chatMessageObj.image);

        holder.tvChatTimestamp.setText(getTime());
        return view;

    }

    static class ViewHolder {
        @BindView(R.id.tvChatMessage)
        TextView tvChatMessage;
        @BindView(R.id.tvChatTimestamp)
        TextView tvChatTimestamp;
        @BindView(R.id.iv_chat_image)
        ImageView ivChatImage;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public String getTime() {
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
        return sdf.format(date);
    }

    public int getCount() {
        return this.chatMessageList.size();
    }

    public ChatMessage getItem(int index) {
        return this.chatMessageList.get(index);
    }

    public void copyText(int index){
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", getItem(index).message.toString());
        clipboard.setPrimaryClip(clip);
    }
    public void deleteMess(int index){
        chatMessageList.remove(index);
        // TODO remove from DB
        notifyDataSetChanged();
    }
}








