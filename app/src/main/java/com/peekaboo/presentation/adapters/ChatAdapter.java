package com.peekaboo.presentation.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.peekaboo.R;
import com.peekaboo.data.repositories.database.messages.PMessageAbs;
import com.peekaboo.domain.DateSeparator;
import com.peekaboo.presentation.app.view.RoundedTransformation;
import com.peekaboo.presentation.presenters.ChatPresenter;
import com.peekaboo.utils.Constants;
import com.peekaboo.utils.Utility;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;
import timber.log.Timber;

/**
 * Created by st1ch on 23.07.2016.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Action1<List<Object>> {

    private final LayoutInflater inflater;
    private Context context;
    private ChatPresenter presenter;
    private Picasso mPicasso;


    private List<Object> messages = Collections.emptyList();

    public interface IChatAdapterListener {
        void toLastMessage();
    }

    public IChatAdapterListener chatAdapterListener;

    public ChatAdapter(Context context, ChatPresenter presenter, Activity activity) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.presenter = presenter;
        this.mPicasso = Picasso.with(context);
        this.chatAdapterListener = (IChatAdapterListener) activity;
    }

    @Override
    public void call(List<Object> messages) {
        this.messages = messages;
        notifyItemInserted(messages.size() - 1);
        chatAdapterListener.toLastMessage();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = getItem(position);
//        if(position != 0){
//            Object previousItem = getItem(position -1);
//            if (previousItem instanceof PMessageAbs && item instanceof PMessageAbs) {
//                if(needSeparator((PMessageAbs) previousItem, (PMessageAbs) item)){
//                    this.messages.add(position, new DateSeparator("April 13"));
//
//                }
//            }
//        }
//        if(item instanceof  PMessageAbs){
//            return ((PMessageAbs) item).mediaType();
//        }
//
//        if(item instanceof DateSeparator){
//            return ((DateSeparator) item).getMediaType();
//        }
//        return -1;
        return ((PMessageAbs) item).mediaType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case Constants.PMESSAGE_MEDIA_TYPE.TEXT_MESSAGE:
                v = inflater.inflate(R.layout.list_item_chat_text_message, parent, false);
                return new ViewHolderText(v);
            case Constants.PMESSAGE_MEDIA_TYPE.AUDIO_MESSAGE:
                v = inflater.inflate(R.layout.list_item_chat_audio_message, parent, false);
                return new ViewHolderAudio(v);
            case Constants.PMESSAGE_MEDIA_TYPE.IMAGE_MESSAGE:
                v = inflater.inflate(R.layout.list_item_chat_image_message, parent, false);
                return new ViewHolderImage(v);
            case Constants.PMESSAGE_MEDIA_TYPE.VIDEO_MESSAGE:
                return null;
            case Constants.PMESSAGE_MEDIA_TYPE.DOCUMENT_MESSAGE:
                return null;
            case DateSeparator.MEDIA_TYPE:
                v = inflater.inflate(R.layout.list_item_dates_separator, parent, false);
                return new DatesViewHolder(v);

        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof DatesViewHolder) {
            ((DatesViewHolder) holder).tvDatesSeparator.setText(((DateSeparator) getItem(position)).getDate());
        } else if (holder instanceof ViewHolder && getItem(position) instanceof PMessageAbs) {

            PMessageAbs pMessageAbs = (PMessageAbs) getItem(position);
            int mediaType = pMessageAbs.mediaType();
            ViewHolder chatHolder = (ChatAdapter.ViewHolder) holder;
            if (position == 0) {
                setAlignment(chatHolder, pMessageAbs.isMine(), true, mediaType);
            } else {
                PMessageAbs pPreviousMessageAbs;
                if(getItem(position - 1) instanceof DateSeparator && getItem(position - 2) != null) {
                    pPreviousMessageAbs = (PMessageAbs) getItem(position - 2);
                } else {
                    pPreviousMessageAbs = (PMessageAbs) getItem(position - 1);
                }

                setAlignment(chatHolder, pMessageAbs.isMine(), pPreviousMessageAbs.isMine(), mediaType);
            }

            switch (mediaType) {
                case Constants.PMESSAGE_MEDIA_TYPE.TEXT_MESSAGE:
                    ((ViewHolderText) holder).tvChatMessage.setText(pMessageAbs.messageBody());
//                ((ViewHolderText) holder).tvChatMessage.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        int lines = ((ViewHolderText) holder).tvChatMessage.getLineCount();
//                        Toast.makeText(context, Integer.toString(lines), Toast.LENGTH_SHORT).show();
//
//                        if(lines == 1){
//                            ((ViewHolderText) holder).chatBubble.setBackgroundResource(R.drawable.round_left_bubble);
//                        }
//                    }
//                });

                    Log.i("TEXT", Integer.toString(mediaType));
                    break;
                case Constants.PMESSAGE_MEDIA_TYPE.AUDIO_MESSAGE:
                    boolean isPlaying = false;
                    ((ViewHolderAudio) holder).ibPlayRecord
                            .setOnClickListener(v
                                    -> {
                                presenter.onStopAndPlayAudioClick(pMessageAbs.messageBody(), position);
                            });
                    ((ViewHolderAudio) holder).sbPlayProgress.setOnTouchListener((v, event) -> true);
                    break;
                case Constants.PMESSAGE_MEDIA_TYPE.IMAGE_MESSAGE:
                    String image = pMessageAbs.messageBody();
                    Timber.tag("IMAGE").wtf("image uri: " + image);
                    setImageMessage((ViewHolderImage) holder, image);
                    Log.i("IMAGE", Integer.toString(mediaType));
                    break;
                case Constants.PMESSAGE_MEDIA_TYPE.VIDEO_MESSAGE:
                    break;
                case Constants.PMESSAGE_MEDIA_TYPE.DOCUMENT_MESSAGE:
                    break;
            }

            chatHolder.tvChatTimestamp.setText(Utility.getFriendlyDayString(context, pMessageAbs.timestamp()));

            setMessageStatus(chatHolder, pMessageAbs);
        }
    }

    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        if(getItem(position) instanceof  PMessageAbs) {
            return ((PMessageAbs) getItem(position)).id();
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    private void setAlignment(ViewHolder holder, boolean isMine, boolean wasPreviousMine, int mediaType) {
        Log.i("ALIGMENT", Integer.toString(mediaType));

        if (!isMine) {
            if (wasPreviousMine == isMine || mediaType == Constants.PMESSAGE_MEDIA_TYPE.IMAGE_MESSAGE) {
                holder.chatBubble.setBackgroundResource(R.drawable.left_bubble);
            } else {
                holder.chatBubble.setBackgroundResource(R.drawable.circle_gray_corner);
            }
            holder.tvChatTimestamp.setTextColor(context.getResources().getColor(R.color.drawerDividerColor));

            RelativeLayout.LayoutParams layoutParams
                    = (RelativeLayout.LayoutParams) holder.chatBubble.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            layoutParams.setMargins(Constants.DESIGN_CONSTANTS.SIDE_MARGIN, Constants.DESIGN_CONSTANTS.TOP_OR_BOTTOM_MARGIN,
                    Constants.DESIGN_CONSTANTS.BIG_SIDE_MARGIN, Constants.DESIGN_CONSTANTS.TOP_OR_BOTTOM_MARGIN);
            holder.chatBubble.setLayoutParams(layoutParams);
        } else {
            if (wasPreviousMine != isMine || mediaType == Constants.PMESSAGE_MEDIA_TYPE.IMAGE_MESSAGE) {
                holder.chatBubble.setBackgroundResource(R.drawable.right_bubble);
            } else {
                holder.chatBubble.setBackgroundResource(R.drawable.circle_blue_corner);
            }

            RelativeLayout.LayoutParams layoutParams
                    = (RelativeLayout.LayoutParams) holder.chatBubble.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            layoutParams.setMargins(Constants.DESIGN_CONSTANTS.BIG_SIDE_MARGIN, Constants.DESIGN_CONSTANTS.TOP_OR_BOTTOM_MARGIN,
                    Constants.DESIGN_CONSTANTS.SIDE_MARGIN, Constants.DESIGN_CONSTANTS.TOP_OR_BOTTOM_MARGIN);
            holder.chatBubble.setLayoutParams(layoutParams);
        }

    }

    private void setMessageStatus(ViewHolder holder, PMessageAbs message) {
        if (message.isSent() && !message.isDelivered()) {
            holder.ivChatMessageStatus.setVisibility(View.GONE);
        } else {
            holder.ivChatMessageStatus.setImageResource(getStatusImage(message.isRead()));
        }
    }

    private int getStatusImage(boolean isRead) {
        return isRead ? R.drawable.ic_check_all : R.drawable.ic_check;
    }

    private void setImageMessage(ChatAdapter.ViewHolderImage holder, String imageUri) {
        holder.pbLoadingImage.setVisibility(View.VISIBLE);
        mPicasso.load(imageUri).resizeDimen(R.dimen.chat_image_width, R.dimen.chat_image_height)
                .error(R.drawable.ic_alert_circle_outline)
                .centerInside()
                .transform(new RoundedTransformation(25, 0))
                .into(holder.ivImageMessage, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                        holder.pbLoadingImage.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        holder.pbLoadingImage.setVisibility(View.GONE);
                    }

                });
    }

    public void clearList() {
        this.messages = Collections.emptyList();
        notifyDataSetChanged();
    }

    public void updateAudioProgress(RecyclerView.ViewHolder holder, long totalDuration, long currentDuration, int progress) {
//        ((ViewHolderAudio) holder).tvTotalDuration.setText(String.valueOf(Utility.milliSecondsToTimer(totalDuration)));
        ((ViewHolderAudio) holder).tvCurrentDuration.setText(String.valueOf(Utility.milliSecondsToTimer(currentDuration)));
        ((ViewHolderAudio) holder).sbPlayProgress.setProgress(progress);
    }

    public void swithPlayButtonImage(RecyclerView.ViewHolder holder, boolean toPlay){
        if(toPlay){
            ((ViewHolderAudio) holder).ibPlayRecord.setBackgroundResource(R.drawable.play_blue);
        } else {
            ((ViewHolderAudio) holder).ibPlayRecord.setBackgroundResource(R.drawable.pause_blue);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        presenter.onDetachedFromRecyclerView();
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
        @BindView(R.id.seekBarPlayProgress)
        SeekBar sbPlayProgress;
        @BindView(R.id.tvCurrentDuration)
        TextView tvCurrentDuration;
//        @BindView(R.id.tvTotalDuration)
//        TextView tvTotalDuration;

        public ViewHolderAudio(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolderImage extends ViewHolder {
        @BindView(R.id.ivImageMessage)
        ImageView ivImageMessage;
        @BindView(R.id.pbLoadingImage)
        ProgressBar pbLoadingImage;

        public ViewHolderImage(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class DatesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.dates_separator_text_view)
        TextView tvDatesSeparator;

        public DatesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}