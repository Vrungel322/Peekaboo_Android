package com.peekaboo.presentation.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.peekaboo.R;
import com.peekaboo.data.repositories.database.PMessageAbs;
import com.peekaboo.presentation.presenters.ChatPresenter;
import com.peekaboo.utils.Constants;
import com.peekaboo.utils.Utility;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by st1ch on 23.07.2016.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> implements Action1<List<PMessageAbs>> {

    private final LayoutInflater inflater;
    private Context context;
    private ChatPresenter chatPresenter;

    private List<PMessageAbs> messages = Collections.emptyList();

    public ChatAdapter(Context context, ChatPresenter chatPresenter) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.chatPresenter = chatPresenter;
    }

    @Override
    public void call(List<PMessageAbs> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).mediaType();
    }

    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        switch (viewType) {
            case Constants.PMESSAGE_MEDIA_TYPE.TEXT_MESSAGE:
                v = LayoutInflater.from(context).inflate(R.layout.list_item_chat_text_message, parent, false);
                return new ViewHolderText(v);
            case Constants.PMESSAGE_MEDIA_TYPE.AUDIO_MESSAGE:
                v = LayoutInflater.from(context).inflate(R.layout.list_item_chat_audio_message, parent, false);
                return new ViewHolderAudio(v);
            case Constants.PMESSAGE_MEDIA_TYPE.IMAGE_MESSAGE:
                v = LayoutInflater.from(context).inflate(R.layout.list_item_chat_image_message, parent, false);
                return new ViewHolderImage(v);
            case Constants.PMESSAGE_MEDIA_TYPE.VIDEO_MESSAGE:
                return null;
            case Constants.PMESSAGE_MEDIA_TYPE.DOCUMENT_MESSAGE:
                return null;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(ChatAdapter.ViewHolder holder, int position) {
        PMessageAbs pMessageAbs = getItem(position);
        setAlignment(holder, pMessageAbs.isMine());

        int mediaType = pMessageAbs.mediaType();
        switch (mediaType) {
            case Constants.PMESSAGE_MEDIA_TYPE.TEXT_MESSAGE:
                ((ViewHolderText) holder).tvChatMessage.setText(pMessageAbs.messageBody());
                break;
            case Constants.PMESSAGE_MEDIA_TYPE.AUDIO_MESSAGE:
                ((ViewHolderAudio) holder).ibPlayRecord
                        .setOnClickListener(v -> chatPresenter.stopAndStartPlayingMPlayer(pMessageAbs.messageBody()));
                break;
            case Constants.PMESSAGE_MEDIA_TYPE.IMAGE_MESSAGE:
                ((ViewHolderImage) holder).ivImageMessage.setImageURI(Uri.parse(pMessageAbs.messageBody()));
                break;
            case Constants.PMESSAGE_MEDIA_TYPE.VIDEO_MESSAGE:
                break;
            case Constants.PMESSAGE_MEDIA_TYPE.DOCUMENT_MESSAGE:
                break;
        }

        holder.tvChatTimestamp.setText(Utility.getFriendlyDayString(context, pMessageAbs.timestamp()));

        setMessageStatus(holder, pMessageAbs);

    }

    public PMessageAbs getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id();
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    private void setAlignment(ViewHolder holder, boolean isMine) {
        if (!isMine) {
           // holder.chatBubble.setBackgroundResource(R.drawable.left);
            holder.tvChatTimestamp.setTextColor(context.getResources().getColor(R.color.drawerDividerColor));

            RelativeLayout.LayoutParams layoutParams
                    = (RelativeLayout.LayoutParams) holder.chatBubble.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.chatBubble.setLayoutParams(layoutParams);
        } else {
            //holder.chatBubble.setBackgroundResource(R.drawable.right);

            RelativeLayout.LayoutParams layoutParams
                    = (RelativeLayout.LayoutParams) holder.chatBubble.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.chatBubble.setLayoutParams(layoutParams);
        }
    }

    private void setMessageStatus(ViewHolder holder, PMessageAbs message){
        if (message.isSent() && !message.isDelivered()) {
            holder.ivChatMessageStatus.setVisibility(View.GONE);
        } else {
            holder.ivChatMessageStatus.setImageResource(getStatusImage(message.isRead()));
        }
    }

    private int getStatusImage(boolean isRead) {
        return isRead ? R.drawable.ic_check_all : R.drawable.ic_check;
    }

    public void clearList() {
        this.messages = Collections.emptyList();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvChatTimestamp)
        TextView tvChatTimestamp;
        @BindView(R.id.ivChatMessageStatus)
        ImageView ivChatMessageStatus;
        @BindView(R.id.chat_bubble)
        FrameLayout chatBubble;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolderText extends ViewHolder {
        @BindView(R.id.tvChatMessage)
        TextView tvChatMessage;

        public ViewHolderText(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolderAudio extends ViewHolder {
        @BindView(R.id.ibPlayRecord)
        ImageButton ibPlayRecord;

        public ViewHolderAudio(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolderImage extends ViewHolder {
        @BindView(R.id.ivImageMessage)
        ImageView ivImageMessage;

        public ViewHolderImage(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}