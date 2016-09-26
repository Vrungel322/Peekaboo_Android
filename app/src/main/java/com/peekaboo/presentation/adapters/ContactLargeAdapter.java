package com.peekaboo.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.peekaboo.R;
import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.presentation.widget.RecyclerViewFastScroller.BubbleTextGetter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class ContactLargeAdapter extends RecyclerView.Adapter<ContactLargeAdapter.ViewHolder>
        implements BubbleTextGetter {
    private final List<Contact> items = new ArrayList<Contact>();

    public ContactLargeAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Contact contact = getItem(position);

    }

    @Override
    public String getTextToShowInBubble(final int pos) {
        return Character.toString(items.get(pos).contactNickname().charAt(0));
    }

    public void setItems(List<Contact> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Contact getItem(int position){
        return items.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.contact_name_text_view)
        TextView tvContactName;
        @BindView(R.id.contact_avatar_image_view)
        ImageView ivAvatar;
        @BindView(R.id.unread_count_text_view)
        TextView tvUnreadCount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
        }

    }
}
