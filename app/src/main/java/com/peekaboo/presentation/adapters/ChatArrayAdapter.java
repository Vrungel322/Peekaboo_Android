package com.peekaboo.presentation.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.peekaboo.R;
import com.peekaboo.presentation.database.PMessage;
import com.peekaboo.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

///**
// * Created by Nataliia on 13.07.2016.
// */
public class ChatArrayAdapter extends ArrayAdapter<PMessage> {

    private List<PMessage> chatMessageList = new ArrayList<PMessage>();
    private Context context;

    @Override
    public void add(PMessage object) {
        chatMessageList.add(object);
        super.add(object);
    }

    public ChatArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    public View getView(int position, View view, ViewGroup parent) {
        PMessage pMessageObj = getItem(position);
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_chat_message, parent, false);


            holder = new ViewHolder(view);
            view.setTag(holder);

            setAlignment(holder, pMessageObj.isMine());
        }

        holder.tvChatMessage.setText(pMessageObj.getMessageBody());
        holder.tvChatTimestamp.setText(Utility.getFriendlyDayString(context, pMessageObj.getTimestamp()));

        if(pMessageObj.isSent() && !pMessageObj.isDelivered()){
            holder.ivChatImage.setVisibility(View.GONE);
        } else {
            holder.ivChatImage.setImageResource(getStatusImage(pMessageObj.isRead()));
        }

        return view;

    }

    private void setAlignment(ViewHolder holder, boolean isMine) {
        if (!isMine) {
            holder.chatBubble.setBackgroundResource(R.drawable.bubble1);

            RelativeLayout.LayoutParams layoutParams
                    = (RelativeLayout.LayoutParams) holder.chatBubble.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.chatBubble.setLayoutParams(layoutParams);
        } else {
            holder.chatBubble.setBackgroundResource(R.drawable.bubble2);

            RelativeLayout.LayoutParams layoutParams
                    = (RelativeLayout.LayoutParams) holder.chatBubble.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.chatBubble.setLayoutParams(layoutParams);
        }
    }

    private int getStatusImage(boolean isRead){
        return isRead ? R.drawable.ic_check_all : R.drawable.ic_check;
    }

    public void copyText(int index) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", getItem(index).getMessageBody());
        clipboard.setPrimaryClip(clip);
    }

    public void deleteMess(int index) {
        chatMessageList.remove(index);
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.chatMessageList.size();
    }

    public PMessage getItem(int index) {
        return this.chatMessageList.get(index);
    }

    static class ViewHolder {
        @BindView(R.id.tvChatMessage)
        TextView tvChatMessage;
        @BindView(R.id.tvChatTimestamp)
        TextView tvChatTimestamp;
        @BindView(R.id.ivChatMessageStatus)
        ImageView ivChatImage;
        @BindView(R.id.chat_bubble)
        FrameLayout chatBubble;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}








