package com.peekaboo.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.peekaboo.R;
import com.peekaboo.presentation.pojo.HotFriendPOJO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Nikita on 31.08.2016.
 */
public class HotFriendsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HotFriendPOJO> hotFriendPOJOs;
    private LayoutInflater inflater;

    public HotFriendsAdapter(Context context, ArrayList<HotFriendPOJO> hotFriendPOJOs) {
        this.context = context;
        this.hotFriendPOJOs = hotFriendPOJOs;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return hotFriendPOJOs.size();
    }

    @Override
    public HotFriendPOJO getItem(int position) {
        return hotFriendPOJOs.get(position);
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

        HotFriendPOJO currentListData = getItem(position);

        mViewHolder.civHotFriendIcon.setImageResource(currentListData.getIconId());
        if (currentListData.getIsOnline()) {
            mViewHolder.civHotFriendStatus.setImageResource(R.drawable.round_status_icon_cyan);
        } else {
            mViewHolder.civHotFriendStatus.setImageResource(R.drawable.round_status_icon_grey);
        }



        return convertView;
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
