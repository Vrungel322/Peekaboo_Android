package com.peekaboo.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.peekaboo.R;
import com.peekaboo.domain.Sms;
import com.peekaboo.domain.SmsDialog;
import com.peekaboo.presentation.activities.MainActivity;
import com.peekaboo.presentation.pojo.PhoneContactPOJO;
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

        long timestamp = lastMessage.getDate();
        // todo write converting timestamp
        holder.tvTimestamp.setText(Utility.getFriendlyDayString(holder.itemView.getContext(), timestamp));

        int unreadMessagesCount = dialog.getUnreadMessagesCount();
        if(unreadMessagesCount > 0){
            holder.tvUnreadCount.setText(String.valueOf(unreadMessagesCount));
        } else {
            holder.tvUnreadCount.setText(null);
        }

        holder.itemView.setOnClickListener(v -> navigator.startSmsChatFragment(activity, contact, true));
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
        @BindView(R.id.sms_contact_avatar_image_view)
        CircleImageView ivAvatar;
        @BindView(R.id.unread_count_text_view)
        TextView tvUnreadCount;
        @BindView(R.id.contact_name_text_view)
        TextView tvContactName;
        @BindView(R.id.message_preview_text_view)
        TextView tvMessagePreview;
        @BindView(R.id.timestamp_text_view)
        TextView tvTimestamp;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
