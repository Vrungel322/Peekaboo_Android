package com.peekaboo.presentation.adapters;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.peekaboo.R;
import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.presentation.utils.ResourcesUtils;
import com.peekaboo.presentation.widget.RecyclerViewFastScroller.BubbleTextGetter;
import com.peekaboo.utils.ActivityNavigator;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public final class ContactLargeAdapter extends RecyclerView.Adapter<ContactLargeAdapter.ViewHolder>
        implements BubbleTextGetter {

    private AppCompatActivity activity;
    private final List<Contact> items = new ArrayList<>();
    private Picasso mPicasso;
    private ActivityNavigator navigator;

    public ContactLargeAdapter(AppCompatActivity activity, ActivityNavigator navigator, Picasso mPicasso) {
        this.activity = activity;
        this.navigator = navigator;
        this.mPicasso = mPicasso;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Contact contact = getItem(position);
        int avatarSize = ResourcesUtils.getDimenInPx(activity, R.dimen.contact_list_avatar_size);

//        mPicasso.load(contact.contactImgUri())
        Picasso.with(activity)
                .load("https://secure.gravatar.com/avatar/67283abf3e13430b424e4e3e8a2233c7?s=64&d=mm&r=g")
//                .load(contact.contactImgUri())
                .tag(activity)
                .resize(0, avatarSize)
                .error(R.drawable.ic_alert_circle_outline)
//                .centerInside()
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

        String contactName = contact.contactName();
        String contactSurname = contact.contactSurname();
        if (contactSurname == null) {
            holder.tvContactName.setText(contactName);
        } else {
            holder.tvContactName.setText(contactName + " " + contactSurname);
        }
        if (contact.isOnline()) {
            holder.ivStatus.setBackgroundResource(R.drawable.list_online_indicator);
        } else {
            holder.ivStatus.setBackgroundResource(R.drawable.list_offline_indicator);
        }

        holder.itemView.setOnClickListener(v -> {
            navigator.startChatFragment(activity, contact, true);
        });

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

    private Contact getItem(int position) {
        return items.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.contact_name_text_view)
        TextView tvContactName;
        @BindView(R.id.contact_avatar_image_view)
        CircleImageView ivAvatar;
        @BindView(R.id.contact_status_image_view)
        View ivStatus;
        @BindView(R.id.loading_image_progress_bar)
        ProgressBar pbImageLoading;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
