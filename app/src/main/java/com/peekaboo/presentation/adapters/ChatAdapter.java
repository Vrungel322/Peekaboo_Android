package com.peekaboo.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.peekaboo.R;
import com.peekaboo.data.repositories.database.PMessageAbs;
import com.peekaboo.utils.Utility;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by st1ch on 23.07.2016.
 */
public class ChatAdapter extends BaseAdapter implements Action1<List<PMessageAbs>> {

    private final LayoutInflater inflater;
    private Context context;

    private List<PMessageAbs> messages = Collections.emptyList();

    public ChatAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public void call(List<PMessageAbs> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public PMessageAbs getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        PMessageAbs pMessageObj = getItem(position);
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
            setAlignment(holder, pMessageObj.isMine());
        } else {
            view = inflater.inflate(R.layout.list_item_chat_message, parent, false);

            holder = new ViewHolder(view);
            view.setTag(holder);
            setAlignment(holder, pMessageObj.isMine());
        }

        holder.tvChatMessage.setText(pMessageObj.messageBody());
        holder.tvChatTimestamp.setText(Utility.getFriendlyDayString(context, pMessageObj.timestamp()));

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

    public void clearList(){
        this.messages = Collections.emptyList();
        notifyDataSetChanged();
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