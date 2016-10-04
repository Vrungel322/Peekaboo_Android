package com.peekaboo.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.peekaboo.R;
import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.data.repositories.database.messages.PMessageAbs;
import com.peekaboo.domain.Dialog;
import com.peekaboo.presentation.activities.MainActivity;
import com.peekaboo.presentation.utils.ResourcesUtils;
import com.peekaboo.utils.ActivityNavigator;
import com.peekaboo.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public final class DialogsLargeAdapter extends RecyclerView.Adapter<DialogsLargeAdapter.ViewHolder> {

    private MainActivity activity;
    private ActivityNavigator navigator;
    private Picasso mPicasso;
    private List<Dialog> items = new ArrayList<>();

    public DialogsLargeAdapter(MainActivity activity, ActivityNavigator navigator) {
        this.activity = activity;
        this.navigator = navigator;
        this.mPicasso = Picasso.with(this.activity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dialog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Dialog dialog = getItem(position);
        Contact contact = dialog.getContact();
        PMessage lastMessage = dialog.getLastMessage();
        final boolean[] muted = {false};
        final boolean[] stared = {false};

        int avatarSize = ResourcesUtils.getDimenInPx(activity, R.dimen.contact_list_avatar_size);

        mPicasso.load(contact.contactImgUri())
                .resize(0, avatarSize)
                .error(R.drawable.ic_alert_circle_outline)
                .into(holder.ivAvatar);

        String contactName = contact.contactName();
        String contactSurname = contact.contactSurname();
        if (contactSurname == null) {
            holder.tvContactName.setText(contactName);
        } else {
            holder.tvContactName.setText(contactName + " " + contactSurname);
        }

        setMessageBody(holder, lastMessage);

        long timestamp = lastMessage.timestamp();
        // todo write converting timestamp
        holder.tvTimestamp.setText(Utility.getFriendlyDayString(holder.itemView.getContext(), timestamp));

        setMessageStatus(holder, lastMessage);

        if (contact.isOnline()) {
            holder.tvUnreadCount.setBackgroundResource(R.drawable.circle_online);
        } else {
            holder.tvUnreadCount.setBackgroundResource(R.drawable.circle_offline);
        }

        int unreadMessagesCount = contact.getUnreadMessagesCount();
        if(unreadMessagesCount > 0){
            holder.tvUnreadCount.setText(String.valueOf(unreadMessagesCount));
        } else {
            holder.tvUnreadCount.setText(null);
        }

        holder.itemView.setOnClickListener(v -> {
            navigator.startChatActivity(activity, contact);
        });

        holder.ivFavorite.setOnClickListener(v -> {
            YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(holder.ivFavorite);
//                items.set(0,getItem(position));
            if (stared[0] == false) {
                stared[0] = true;
                holder.ivFavorite.setImageResource(R.drawable.stared);
            } else {
                stared[0] = false;
                holder.ivFavorite.setImageResource(R.drawable.star);

            }

        });
        holder.ivDelete.setOnClickListener(v -> {
            YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(holder.ivDelete);
            delete(position);
        });
        holder.ivMute.setOnClickListener(v -> {

            YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(holder.ivMute);

            if (muted[0] == false) {
                muted[0] = true;
                holder.ivMute.setImageResource(R.drawable.nosound);
            } else {
                muted[0] = false;
                holder.ivMute.setImageResource(R.drawable.sound);
            }

        });


    }


    private void delete(int position){
        items.remove(position);
        notifyItemRemoved(position);
        }

    private void setMessageBody(ViewHolder holder, PMessage message){
        int messageType = message.mediaType();
        switch (messageType) {
            case PMessageAbs.PMESSAGE_MEDIA_TYPE.TEXT_MESSAGE:
                holder.tvMessagePreview.setText(message.messageBody());
                break;
            case PMessageAbs.PMESSAGE_MEDIA_TYPE.AUDIO_MESSAGE:
                holder.tvMessagePreview.setText(getStringResource(holder, R.string.message_type_audio));
                break;
            case PMessageAbs.PMESSAGE_MEDIA_TYPE.IMAGE_MESSAGE:
                holder.tvMessagePreview.setText(getStringResource(holder, R.string.message_type_image));
                break;
            case PMessageAbs.PMESSAGE_MEDIA_TYPE.VIDEO_MESSAGE:
                holder.tvMessagePreview.setText(getStringResource(holder, R.string.message_type_video));
                break;
            case PMessageAbs.PMESSAGE_MEDIA_TYPE.DOCUMENT_MESSAGE:
                holder.tvMessagePreview.setText(getStringResource(holder, R.string.message_type_document));
                break;
        }
    }


    private String getStringResource(ViewHolder holder, int id){
        return holder.itemView.getContext().getString(id);
    }

    private void setMessageStatus(ViewHolder holder, PMessage message) {
        if (!message.isMine() || message.status() == PMessageAbs.PMESSAGE_STATUS.STATUS_SENT) {
            holder.ivMessageStatus.setVisibility(View.GONE);
        } else {
            holder.ivMessageStatus.setImageResource(getStatusImage(message.status()));
            holder.ivMessageStatus.setVisibility(View.VISIBLE);
        }
    }

    private int getStatusImage(int status) {
        return status == PMessage.PMESSAGE_STATUS.STATUS_READ ? R.drawable.ic_check_all : R.drawable.ic_check;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Dialog getItem(int position) {
        return items.get(position);
    }

    public void setItems(List<Dialog> dialogs) {
        items.clear();
        items.addAll(dialogs);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.contact_avatar_image_view)
        CircleImageView ivAvatar;
        @BindView(R.id.unread_count_text_view)
        TextView tvUnreadCount;
        @BindView(R.id.contact_name_text_view)
        TextView tvContactName;
        @BindView(R.id.message_preview_text_view)
        TextView tvMessagePreview;
        @BindView(R.id.timestamp_text_view)
        TextView tvTimestamp;
        @BindView(R.id.message_status_image_view)
        ImageView ivMessageStatus;
        @BindView(R.id.delete_image_view)
        ImageView ivDelete;
        @BindView(R.id.favourite_image_view)
        ImageView ivFavorite;
        @BindView(R.id.mute_image_view)
        ImageView ivMute;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }
}
