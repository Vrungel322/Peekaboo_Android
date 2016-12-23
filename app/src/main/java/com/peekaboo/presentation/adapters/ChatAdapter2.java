package com.peekaboo.presentation.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.peekaboo.R;
import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.data.repositories.database.messages.PMessageAbs;
import com.peekaboo.presentation.app.view.RoundedTransformation;
import com.peekaboo.presentation.presenters.ChatPresenter2;
import com.peekaboo.presentation.utils.AudioIdManager;
import com.peekaboo.presentation.utils.AudioPlayer;
import com.peekaboo.presentation.utils.ResourcesUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sebastian on 08.09.16.
 */
public class ChatAdapter2 extends RecyclerView.Adapter<ChatAdapter2.ViewHolder> {

    private static final int READ_DELAY = 100;
    private final Context context;
    private final LayoutInflater inflater;
    private final ChatPresenter2 presenter;
    private final Picasso mPicasso;
    private final int bubbleSmallHorizontalMargin;
    private final int bubbleLargeHorizontalMargin;
    private final int bubbleVerticalMargin;
    private final int bubbleMaxWidth;
    private final int imageMaxHeight;
    private Contact contact;
    private final List<PMessage> messages = new ArrayList<>();
    private RecyclerView recyclerView;
    private Handler handler;
    private AudioPlayer.AudioPlayerListener playerListener = new AudioPlayer.AudioPlayerListener() {

        @Override
        public void onStartPlaying(String audioId) {

            long id = AudioIdManager.getMessageId(audioId);
            String companionId = AudioIdManager.getCompanionId(audioId);
            if (contact.contactId().equals(companionId)) {
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForItemId(id);
                if (viewHolder != null && viewHolder instanceof ViewHolderAudio) {
                    ViewHolderAudio viewHolderAudio = (ViewHolderAudio) viewHolder;
                    viewHolderAudio.ibPlayRecord.setImageResource(R.drawable.pause_blue);
                }
            }
        }

        @Override
        public void onStopPlaying(String audioId) {
            long id = AudioIdManager.getMessageId(audioId);
            String companionId = AudioIdManager.getCompanionId(audioId);
            if (contact.contactId().equals(companionId)) {
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForItemId(id);
                if (viewHolder != null && viewHolder instanceof ViewHolderAudio) {
                    ViewHolderAudio viewHolderAudio = (ViewHolderAudio) viewHolder;
                    viewHolderAudio.ibPlayRecord.setImageResource(R.drawable.play_blue);
                }
            }
        }

        @Override
        public void onProgressChanged(String audioId, long position, long duration) {
            long id = AudioIdManager.getMessageId(audioId);
            String companionId = AudioIdManager.getCompanionId(audioId);
            if (contact.contactId().equals(companionId)) {
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForItemId(id);
                if (viewHolder != null && viewHolder instanceof ViewHolderAudio) {
                    ViewHolderAudio viewHolderAudio = (ViewHolderAudio) viewHolder;
                    int max = (int) (duration / 100);
                    int pos = (int) (position / 100);
                    Log.e("adapter", System.currentTimeMillis() + " progress " + pos + " " + max);
                    viewHolderAudio.sbPlayProgress.setMax(max);
                    viewHolderAudio.sbPlayProgress.setProgress(pos);
                    viewHolderAudio.tvCurrentDuration.setText(String.format("%02d:%02d", (pos / 10) / 60, (pos / 10) % 60));
                }
            }
        }
    };
    @Nullable
    private OnItemLongClickListener onItemLongClickListener;
    @Nullable
    private OnItemClickListener onItemClickListener;

    private int imageMessageRadius;
    
    public ChatAdapter2(Context context, ChatPresenter2 presenter, RecyclerView recyclerView, Contact contact,
                        Picasso mPicasso) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.inflater = LayoutInflater.from(context);
        this.presenter = presenter;
        this.mPicasso = mPicasso;
        this.contact = contact;
        handler = new Handler();
        imageMessageRadius = ResourcesUtils.getDimenInPx(context, R.dimen.bubble_corner_radius_code);
        bubbleSmallHorizontalMargin = ResourcesUtils.getDimenInPx(context, R.dimen.chat_small_horizontal_margin);
        bubbleLargeHorizontalMargin = ResourcesUtils.getDimenInPx(context, R.dimen.chat_large_horizontal_margin);
        bubbleVerticalMargin = ResourcesUtils.getDimenInPx(context, R.dimen.chat_vertical_margin);
        bubbleMaxWidth = ResourcesUtils.getDisplayWidth(context) - bubbleLargeHorizontalMargin - bubbleSmallHorizontalMargin
                - 2 * (ResourcesUtils.getDimenInPx(context, R.dimen.chat_horizontal_margin) + ResourcesUtils.getDimenInPx(context, R.dimen.chat_image_message_padding));
        imageMaxHeight = ResourcesUtils.getDimenInPx(context, R.dimen.chat_image_height);
        Log.e("ChatAdapter", bubbleMaxWidth + " " + imageMaxHeight);
        setHasStableIds(true);
    }


    @Override
    public int getItemViewType(int position) {
        return getItem(position).mediaType();
    }

    @Override
    public ChatAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case PMessageAbs.PMESSAGE_MEDIA_TYPE.TEXT_MESSAGE:
                v = inflater.inflate(R.layout.list_item_chat_text_message, parent, false);
                return new ViewHolderText(v);
            case PMessageAbs.PMESSAGE_MEDIA_TYPE.AUDIO_MESSAGE:
                v = inflater.inflate(R.layout.list_item_chat_audio_message, parent, false);
                return new ViewHolderAudio(v);
            case PMessageAbs.PMESSAGE_MEDIA_TYPE.IMAGE_MESSAGE:
                v = inflater.inflate(R.layout.list_item_chat_image_message, parent, false);
                return new ViewHolderImage(v);
            case PMessageAbs.PMESSAGE_MEDIA_TYPE.GEO_MESSAGE:
                v = inflater.inflate(R.layout.list_item_chat_image_message, parent, false);
                return new ViewHolderGeo(v);
            case PMessageAbs.PMESSAGE_MEDIA_TYPE.VIDEO_MESSAGE:
                v = inflater.inflate(R.layout.list_item_chat_image_message, parent, false);
                return new ViewHolderGeo(v);
        }

        return null;
    }

    public PMessage getItem(int position) {
        return messages.get(position);
    }


    @Override
    public void onBindViewHolder(ChatAdapter2.ViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        PMessage pMessageAbs = getItem(position);
        int mediaType = pMessageAbs.mediaType();
        Log.wtf("mediaType : ", String.valueOf(mediaType));
        final int finalPosition = position;
        holder.itemView.setOnLongClickListener(view -> {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onLongClick(finalPosition);
            }
            return false;
        });
        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(finalPosition);
            }
//            return false;
        });
        boolean nextMine;
        boolean prevMine;

        if (position < getItemCount() - 1) {
            nextMine = getItem(position + 1).isMine();
        } else {
            nextMine = false;
        }

        if (position > 0) {
            prevMine = getItem(position - 1).isMine();
        } else {
            prevMine = true;
        }

        setAlignment(holder, pMessageAbs.isMine(), prevMine, nextMine);

        switch (mediaType) {
            case PMessageAbs.PMESSAGE_MEDIA_TYPE.GEO_MESSAGE:
                if (holder instanceof ViewHolderGeo) {
                    String link = pMessageAbs.messageBody();
                    Log.wtf("geo link : ", link);
                    setGeoMessage((ViewHolderGeo) holder, link);

                }
                break;
            case PMessageAbs.PMESSAGE_MEDIA_TYPE.TEXT_MESSAGE:
                if (holder instanceof ViewHolderText) {
                    ((ViewHolderText) holder).tvChatMessage.setText(Html.fromHtml(pMessageAbs.messageBody()));
                }
                break;
            case PMessageAbs.PMESSAGE_MEDIA_TYPE.AUDIO_MESSAGE:
                if (holder instanceof ViewHolderAudio) {
                    ViewHolderAudio holderAudio = (ViewHolderAudio) holder;
                    presenter.setPlayerListener(playerListener);
                    Log.e("adapter", pMessageAbs.id() + " " + pMessageAbs.messageBody() + " " + pMessageAbs.isMine() + " " + pMessageAbs.hasBothPaths());
                    holderAudio.pbLoad.setVisibility(pMessageAbs.hasBothPaths() || pMessageAbs.hasFileError() ? View.GONE : View.VISIBLE);
                    holderAudio.ibPlayRecord.setVisibility(!pMessageAbs.hasBothPaths() || pMessageAbs.hasFileError() ? View.GONE : View.VISIBLE);
                    holderAudio.ibPlayRecord.setOnClickListener(v -> {
                        presenter.onPlayButtonClick(pMessageAbs, playerListener);
                    });
                }
                break;
            case PMessageAbs.PMESSAGE_MEDIA_TYPE.IMAGE_MESSAGE:
                if (holder instanceof ViewHolderImage) {
                    ViewHolderImage holderImage = (ViewHolderImage) holder;
                    String image = pMessageAbs.messageBody();
                    holderImage.ivImageMessage.setImageBitmap(null);
                    holderImage.pbLoadingImage.setVisibility(pMessageAbs.hasBothPaths() || pMessageAbs.hasFileError() ? View.GONE : View.VISIBLE);
                    if (image.split(PMessage.DIVIDER).length == 2) {
                        Log.wtf("image : ", ResourcesUtils.splitImagePath(image, 2));
                        setImageMessage(holderImage, ResourcesUtils.splitImagePath(image, 2));
                    }
                }
                break;
            case PMessageAbs.PMESSAGE_MEDIA_TYPE.VIDEO_MESSAGE:
                break;
            case PMessageAbs.PMESSAGE_MEDIA_TYPE.DOCUMENT_MESSAGE:
                break;
        }

        setMessageStatus(holder, pMessageAbs);


        boolean isDelivered = pMessageAbs.status() == PMessageAbs.PMESSAGE_STATUS.STATUS_DELIVERED
                && !pMessageAbs.isMine();

        if (isDelivered) {
            handler.postDelayed(() -> presenter.onUserMessageRead(pMessageAbs), READ_DELAY);
        }
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.itemView.setOnLongClickListener(null);
    }

    private void setAlignment(ViewHolder holder, boolean isMine, boolean wasPreviousMine, boolean isNextMine) {
        holder.tvChatTimestamp.setTextColor(ResourcesUtils.getColor(context, isMine ? R.color.colorDarkAccent : R.color.drawerDividerColor));

        RelativeLayout.LayoutParams layoutParams
                = (RelativeLayout.LayoutParams) holder.chatBubble.getLayoutParams();
        layoutParams.addRule(isMine ? RelativeLayout.ALIGN_PARENT_LEFT : RelativeLayout.ALIGN_PARENT_RIGHT, 0);
        layoutParams.addRule(isMine ? RelativeLayout.ALIGN_PARENT_RIGHT : RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.setMargins(
                isMine ? bubbleLargeHorizontalMargin : bubbleSmallHorizontalMargin,
                bubbleVerticalMargin,
                isMine ? bubbleSmallHorizontalMargin : bubbleLargeHorizontalMargin,
                bubbleVerticalMargin
        );
        holder.chatBubble.setLayoutParams(layoutParams);
        if (isMine) {
            if (!isNextMine) {
                holder.chatBubble.setBackgroundResource(R.drawable.circle_blue_corner2);
            } else {
                holder.chatBubble.setBackgroundResource(R.drawable.circle_blue2);
            }
        } else {
            if (wasPreviousMine) {
                holder.chatBubble.setBackgroundResource(R.drawable.circle_gray_corner2);
            } else {
                holder.chatBubble.setBackgroundResource(R.drawable.circle_gray2);
            }
        }
    }

    private void setMessageStatus(ViewHolder holder, PMessage message) {
        if (message.status() == PMessageAbs.PMESSAGE_STATUS.STATUS_SENT) {
            holder.ivChatMessageStatus.setVisibility(View.GONE);
        } else {
            holder.ivChatMessageStatus.setImageResource(getStatusImage(message.status()));
            holder.ivChatMessageStatus.setVisibility(View.VISIBLE);
        }
    }

    private int getStatusImage(int status) {
        return status == PMessage.PMESSAGE_STATUS.STATUS_READ ? R.drawable.ic_check_all : R.drawable.ic_check;
    }

    private void setImageMessage(ChatAdapter2.ViewHolderImage holder, String imageUri) {
//        holder.pbLoadingImage.setVisibility(View.VISIBLE);
        mPicasso.load(Uri.fromFile(new File(imageUri))).resize(bubbleMaxWidth, imageMaxHeight)
                .error(R.drawable.ic_alert_circle_outline)
                .centerInside()
                .transform(new RoundedTransformation(imageMessageRadius, 0))
                .into(holder.ivImageMessage, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
//                        holder.pbLoadingImage.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
//                        holder.pbLoadingImage.setVisibility(View.GONE);
                    }

                });
    }

    private void setGeoMessage(ChatAdapter2.ViewHolderGeo holder, String link) {
        holder.pbLoadingImage.setVisibility(View.VISIBLE);

        mPicasso.load(Uri.parse(link)).resize(bubbleMaxWidth, imageMaxHeight)
                .error(R.drawable.ic_alert_circle_outline)
                .centerInside()
                .transform(new RoundedTransformation(imageMessageRadius, 0))
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


    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id();
    }

    public void setMessages(List<PMessage> messages) {
        this.messages.clear();
        this.messages.addAll(messages);
        notifyDataSetChanged();
        scrollToLastMessage();
    }

    private void scrollToLastMessage() {
        recyclerView.scrollToPosition(getItemCount() - 1);
    }

    public void appendMessages(List<PMessage> messages) {
        int size = getItemCount();
        this.messages.addAll(messages);
        notifyItemRangeInserted(size, messages.size());
        notifyItemChanged(size - 1);
        scrollToLastMessage();
    }

    public void updateMessage(PMessage message) {
        int size = messages.size();
        if (size == 0 || message.id() > messages.get(size - 1).id()) {
            Log.e("adapter", "append " + size + " " + message);
            messages.add(message);
            notifyItemInserted(size);
            notifyItemChanged(size - 1);
            scrollToLastMessage();
        } else {
            for (int i = messages.size() - 1; i >= 0; i--) {
                PMessage mes = messages.get(i);
                if (mes.id() == message.id()) {
                    Log.e("adapter", "update " + i + " " + message);
                    messages.set(i, message);
                    notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    // RV : optimize allocation of different view holders number
    @Override
    public boolean onFailedToRecycleView(ViewHolder holder) {
        return true;
    }

    public void setOnItemLongClickListener(@Nullable OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }
    public void setOnItemClickListener(@Nullable OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemLongClickListener {
        void onLongClick(int position);
    }
    public interface OnItemClickListener {
        void onClick(int position);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvChatTimestamp)
        TextView tvChatTimestamp;
        @BindView(R.id.ivChatMessageStatus)
        ImageView ivChatMessageStatus;
        @BindView(R.id.chat_bubble)
        FrameLayout chatBubble;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolderText extends ViewHolder {
        @BindView(R.id.tvChatMessage)
        TextView tvChatMessage;

        ViewHolderText(View view) {
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
        @BindView(R.id.pbLoad)
        ProgressBar pbLoad;

        ViewHolderAudio(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    static class ViewHolderImage extends ViewHolder {
        @BindView(R.id.ivImageMessage)
        ImageView ivImageMessage;
        @BindView(R.id.pbLoadingImage)
        ProgressBar pbLoadingImage;

        ViewHolderImage(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolderGeo extends ViewHolder {
        @BindView(R.id.ivImageMessage)
        ImageView ivImageMessage;
        @BindView(R.id.pbLoadingImage)
        ProgressBar pbLoadingImage;

        ViewHolderGeo(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
