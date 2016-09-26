package com.peekaboo.presentation.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.peekaboo.R;
import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.presentation.widget.RecyclerViewFastScroller.BubbleTextGetter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class ContactLargeAdapter extends RecyclerView.Adapter<ContactLargeAdapter.ViewHolder>
        implements BubbleTextGetter {

    private final List<Contact> items = new ArrayList<>();
    private Picasso mPicasso;

    public ContactLargeAdapter(Context context) {
        this.mPicasso = Picasso.with(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Contact contact = getItem(position);

        //todo make caching images to sd and fetching them here not from url
        mPicasso.load(contact.contactImgUri())
                .resize(R.dimen.contact_list_avatar_size, R.dimen.contact_list_avatar_size)
                .error(R.drawable.ic_alert_circle_outline)
                .centerInside()
                .into(holder.ivAvatar, new Callback.EmptyCallback(){
                    @Override
                    public void onSuccess() {
                        super.onSuccess();
                        holder.pbImageLoading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        super.onError();
                        holder.pbImageLoading.setVisibility(View.GONE);
                    }
                });

        holder.tvContactName.setText(contact.contactName() + " " + contact.contactSurname());

        if(contact.isOnline()){
            holder.ivStatus.setImageResource(R.color.online);
        } else {
            holder.ivStatus.setImageResource(R.color.offline);
        }

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
        @BindView(R.id.contact_status_image_view)
        ImageView ivStatus;
        @BindView(R.id.loading_image_progress_bar)
        ProgressBar pbImageLoading;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
        }

    }
}
