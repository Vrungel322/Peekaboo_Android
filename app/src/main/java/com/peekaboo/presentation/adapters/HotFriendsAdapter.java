package com.peekaboo.presentation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.peekaboo.R;
import com.peekaboo.domain.Dialog;
import com.peekaboo.presentation.activities.MainActivity;
import com.peekaboo.utils.ActivityNavigator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Nikita on 31.08.2016.
 */
public class HotFriendsAdapter extends BaseAdapter {

    private MainActivity activity;
    private LayoutInflater inflater;
    private Picasso mPicasso;
    private ActivityNavigator navigator;
    private List<Dialog> items = new ArrayList<>();


    public HotFriendsAdapter(MainActivity activity, Picasso mPicasso, ActivityNavigator navigator) {
        this.activity = activity;
        this.mPicasso = mPicasso;
        this.navigator = navigator;
        inflater = LayoutInflater.from(this.activity);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Dialog getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HotFriendsViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.hot_friend_list_item, parent, false);
            mViewHolder = new HotFriendsViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (HotFriendsViewHolder) convertView.getTag();
        }

        Dialog currentListData = getItem(position);

        mPicasso.load(currentListData.getContact().contactImgUri())
                .into( mViewHolder.civHotFriendIcon);

        if (currentListData.getContact().isOnline()) {
            mViewHolder.civHotFriendStatus.setImageResource(R.drawable.round_status_icon_cyan);
        } else {
            mViewHolder.civHotFriendStatus.setImageResource(R.drawable.round_status_icon_grey);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigator.startChatActivity(activity, currentListData.getContact());
            }
        });
        return convertView;
    }

    public void setItems(List<Dialog> dialogs) {
        items.clear();
        items.addAll(dialogs);
        notifyDataSetChanged();
    }

    static class HotFriendsViewHolder {
        @BindView(R.id.civHotFriendIcon)
        CircleImageView civHotFriendIcon;
        @BindView(R.id.civHotFriendStatus)
        CircleImageView civHotFriendStatus;

        public HotFriendsViewHolder(View item) {
            ButterKnife.bind(this, item);
        }
    }
}
