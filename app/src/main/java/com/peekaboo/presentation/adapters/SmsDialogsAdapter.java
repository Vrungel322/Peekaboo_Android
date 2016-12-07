package com.peekaboo.presentation.adapters;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.peekaboo.R;
import com.peekaboo.domain.Sms;
import com.peekaboo.domain.SmsDialog;
import com.peekaboo.presentation.activities.MainActivity;
import com.peekaboo.presentation.pojo.PhoneContactPOJO;
import com.peekaboo.presentation.utils.AvatarIcon;
import com.peekaboo.utils.ActivityNavigator;
import com.peekaboo.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by st1ch on 19.10.2016.
 */

public class SmsDialogsAdapter extends RecyclerView.Adapter<SmsDialogsAdapter.ViewHolder> {

    private MainActivity activity;
    private ActivityNavigator navigator;
    private List<SmsDialog> items = new ArrayList<>();

    public SmsDialogsAdapter(MainActivity activity, ActivityNavigator navigator) {
        this.activity = activity;
        this.navigator = navigator;
    }

    @Override
    public SmsDialogsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_sms_dialog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SmsDialogsAdapter.ViewHolder holder, int position) {
        SmsDialog dialog = getItem(position);
        PhoneContactPOJO contact = dialog.getContact();
        Sms lastMessage = dialog.getLastMessage();

        String contactName = contact.getName();
        holder.tvContactName.setText(contactName);
        holder.tvMessagePreview.setText(lastMessage.getBody());

        setUpTimestamp(holder, lastMessage);

        setUpBackdroundColor(holder, dialog);

        setUpDefaultAvatarText(contactName, holder);

        setUpUnreadMessageCount(holder, dialog);

        holder.itemView.setOnClickListener(v -> navigator.startSmsChat(activity, contact));
    }

    private void setUpTimestamp(ViewHolder holder, Sms lastMessage) {
        long timestamp = lastMessage.getDate();
        // todo write converting timestamp
        holder.tvTimestamp.setText(Utility.getFriendlyDayString(holder.itemView.getContext(), timestamp));
    }

    private void setUpBackdroundColor(ViewHolder holder, SmsDialog dialog) {
        if (dialog.getUnreadMessagesCount() > 0) {
            holder.dialogContainer.setBackgroundColor(activity.getResources().getColor(R.color.unread_msg_background));
        } else {
            holder.dialogContainer.setBackgroundColor(Color.WHITE);
        }
    }

    private void setUpUnreadMessageCount(ViewHolder holder, SmsDialog dialog) {
        int unreadMessagesCount = dialog.getUnreadMessagesCount();
        if (unreadMessagesCount > 0) {
            holder.tvUnreadCount.setText(String.valueOf(unreadMessagesCount));
        } else {
            holder.tvUnreadCount.setText(null);
        }
    }

    private void setUpDefaultAvatarText(String contactName, SmsDialogsAdapter.ViewHolder holder) {
        String avatarText;
        Drawable drawable = activity.getResources().getDrawable(R.drawable.avatar_icon);
        holder.defaultAvatar.setImageDrawable(AvatarIcon.setDrawableColor(drawable, contactName, null));
        avatarText = contactName.substring(0, 1).toUpperCase();
        holder.defaultAvatarText.setText(avatarText);
    }

    private SmsDialog getItem(int position) {
        return items.get(position);
    }

    public void setItems(List<SmsDialog> dialogs) {
        items.clear();
        items.addAll(dialogs);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.list_item_dialog_container)
        CoordinatorLayout dialogContainer;
        @BindView(R.id.sms_contact_avatar_image_view)
        CircleImageView ivAvatar;
        @BindView(R.id.contact_name_text_view)
        TextView tvContactName;
        @BindView(R.id.message_preview_text_view)
        TextView tvMessagePreview;
        @BindView(R.id.timestamp_text_view)
        TextView tvTimestamp;
        @BindView(R.id.unread_count_text_view)
        TextView tvUnreadCount;
        @BindView(R.id.default_avatar_sms_text)
        TextView defaultAvatarText;
        @BindView(R.id.default_sms_avatar)
        ImageView defaultAvatar;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
