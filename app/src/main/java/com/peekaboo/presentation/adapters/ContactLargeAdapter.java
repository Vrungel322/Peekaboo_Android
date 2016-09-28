package com.peekaboo.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.peekaboo.R;
import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.presentation.activities.MainActivity;
import com.peekaboo.presentation.utils.ResourcesUtils;
import com.peekaboo.presentation.widget.RecyclerViewFastScroller.BubbleTextGetter;
import com.peekaboo.utils.ActivityNavigator;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public final class ContactLargeAdapter extends RecyclerView.Adapter<ContactLargeAdapter.ViewHolder>
        implements BubbleTextGetter {

    private final int onlineColor;
    private final int offlineColor;
    private MainActivity activity;
    private final List<Contact> items = new ArrayList<>();
    private Picasso mPicasso;
    private ActivityNavigator navigator;

    public ContactLargeAdapter(MainActivity activity, ActivityNavigator navigator, Picasso mPicasso) {
        this.activity = activity;
        this.navigator = navigator;
        this.mPicasso = mPicasso;
        onlineColor = ResourcesUtils.getColor(activity, R.color.online);
        offlineColor = ResourcesUtils.getColor(activity, R.color.offline);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Contact contact = getItem(position);

        //todo make caching images to sd and fetching them here not from url
        Log.e("contact", "" + contact.contactImgUri());
        int avatarSize = ResourcesUtils.getDimenInPx(activity, R.dimen.contact_list_avatar_size);

        mPicasso.load(contact.contactImgUri())
                .resize(0, avatarSize)
//                .networkPolicy(NetworkPolicy.OFFLINE)
                .error(R.drawable.ic_alert_circle_outline)
//                .centerInside()
                .into(holder.ivAvatar/*, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                        super.onSuccess();
//                        holder.pbImageLoading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        super.onError();
//                        mPicasso.load(contact.contactImgUri())
//                                .resize(0, avatarSize)
//                                .error(R.drawable.ic_alert_circle_outline)
//                                .into(holder.ivAvatar);
//                        holder.pbImageLoading.setVisibility(View.GONE);
                    }
                }*/);

        String contactName = contact.contactName();
        String contactSurname = contact.contactSurname();
        if (contactSurname == null) {
            holder.tvContactName.setText(contactName);
        } else {
            holder.tvContactName.setText(contactName + " " + contactSurname);
        }
        if (contact.isOnline()) {
            holder.ivStatus.setImageResource(R.drawable.round_status_icon_cyan);
        } else {
            holder.ivStatus.setImageResource(R.drawable.round_status_icon_grey);
        }

        holder.itemView.setOnClickListener(v -> {
            navigator.startChatActivity(activity, contact);
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
        CircleImageView ivStatus;
        @BindView(R.id.loading_image_progress_bar)
        ProgressBar pbImageLoading;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
