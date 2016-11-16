package com.peekaboo.presentation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.peekaboo.R;
import com.peekaboo.domain.Dialog;
import com.peekaboo.presentation.activities.MainActivity;
import com.peekaboo.utils.ActivityNavigator;
import com.squareup.picasso.Callback;
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
        if (items.size() > 9) {
            return 10;
        } else {
            return items.size();
        }
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

//        mPicasso.load(currentListData.getContact().contactImgUri())
        Picasso.with(activity)
                .load("https://secure.gravatar.com/avatar/67283abf3e13430b424e4e3e8a2233c7?s=64&d=mm&r=g")
//                .load(currentListData.getContact().contactImgUri())
                .tag(activity)
                .into(mViewHolder.civHotFriendIcon, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                        super.onSuccess();
                        mViewHolder.loading_hotFriend_progress_bar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        super.onError();
                        mViewHolder.loading_hotFriend_progress_bar.setVisibility(View.GONE);
                    }
                });

        showIfContactIsOnline(mViewHolder, currentListData);

        showUnreadMesCount(mViewHolder, currentListData);

        convertView.setOnClickListener(v ->
                navigator.startChatFragment(activity, currentListData.getContact(), true));
        return convertView;
    }

    private void showIfContactIsOnline(HotFriendsViewHolder mViewHolder, Dialog currentListData) {
        if (currentListData.getContact().isOnline()) {
            mViewHolder.civHotFriendStatus.setBackgroundResource(R.drawable.list_online_indicator);
        } else {
            mViewHolder.civHotFriendStatus.setBackgroundResource(R.drawable.list_offline_indicator);
        }
    }

    private void showUnreadMesCount(HotFriendsViewHolder mViewHolder, Dialog currentListData) {
        if (currentListData.getUnreadMessagesCount() == 0) {
            mViewHolder.tvUnreadMesCountInRightDrawer
                    .setText(null);
        } else {
            mViewHolder.tvUnreadMesCountInRightDrawer
                    .setText(String.valueOf(currentListData.getUnreadMessagesCount()));
        }
    }

    public void setItems(List<Dialog> dialogs) {
        items.clear();
        items.addAll(dialogs);
        notifyDataSetChanged();
    }

    static class HotFriendsViewHolder {
        @BindView(R.id.loading_hotFriend_progress_bar)
        ProgressBar loading_hotFriend_progress_bar;
        @BindView(R.id.civHotFriendIcon)
        CircleImageView civHotFriendIcon;
        @BindView(R.id.civHotFriendStatus)
        View civHotFriendStatus;
        @BindView(R.id.tvUnreadMesCountInRightDrawer)
        TextView tvUnreadMesCountInRightDrawer;

        public HotFriendsViewHolder(View item) {
            ButterKnife.bind(this, item);
        }
    }
}
