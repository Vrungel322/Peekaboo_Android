package com.peekaboo.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.peekaboo.R;
import com.peekaboo.domain.Sms;
import com.peekaboo.presentation.utils.ResourcesUtils;
import com.peekaboo.utils.Constants;
import com.peekaboo.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by st1ch on 21.10.2016.
 */

public class SmsChatAdapter extends RecyclerView.Adapter<SmsChatAdapter.ViewHolder> {

    private List<Sms> smsList = new ArrayList<>();
    private RecyclerView recyclerView;

    public SmsChatAdapter(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_sms_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Sms sms = getItem(position);

        String messageBody = sms.getBody();
        long timestamp = sms.getDate();
        int type = sms.getType();

        boolean nextMine;
        boolean prevMine;

        nextMine = position < getItemCount() - 1 && isMine(getItem(position + 1).getType());

        prevMine = position <= 0 || isMine(getItem(position - 1).getType());

        setAlignment(holder, isMine(type), prevMine, nextMine);
        holder.tvTimestamp.setText(Utility.getFriendlyDayString(holder.itemView.getContext(), timestamp));
        holder.tvMessageBody.setText(messageBody);

    }

    private void setAlignment(ViewHolder holder, boolean isMine, boolean wasPreviousMine, boolean isNextMine) {
        holder.tvTimestamp.setTextColor(ResourcesUtils.getColor(holder.itemView.getContext(),
                isMine ? R.color.colorDarkAccent : R.color.drawerDividerColor));

        RelativeLayout.LayoutParams layoutParams
                = (RelativeLayout.LayoutParams) holder.chatBubble.getLayoutParams();
        layoutParams.addRule(isMine ? RelativeLayout.ALIGN_PARENT_LEFT : RelativeLayout.ALIGN_PARENT_RIGHT, 0);
        layoutParams.addRule(isMine ? RelativeLayout.ALIGN_PARENT_RIGHT : RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.setMargins(
                isMine ? Constants.DESIGN_CONSTANTS.BIG_SIDE_MARGIN : Constants.DESIGN_CONSTANTS.SIDE_MARGIN,
                Constants.DESIGN_CONSTANTS.TOP_OR_BOTTOM_MARGIN,
                isMine ? Constants.DESIGN_CONSTANTS.SIDE_MARGIN : Constants.DESIGN_CONSTANTS.BIG_SIDE_MARGIN,
                Constants.DESIGN_CONSTANTS.TOP_OR_BOTTOM_MARGIN
        );
        holder.chatBubble.setLayoutParams(layoutParams);
        if (isMine) {
            if (!isNextMine) {
                holder.chatBubble.setBackgroundResource(R.drawable.circle_blue_corner);
            } else {
                holder.chatBubble.setBackgroundResource(R.drawable.circle_blue);
            }
        } else {
            if (wasPreviousMine) {
                holder.chatBubble.setBackgroundResource(R.drawable.circle_gray_corner);
            } else {
                holder.chatBubble.setBackgroundResource(R.drawable.circle_gray);
            }
        }
    }

    private boolean isMine(int type){
        return type == 2;
    }

    public Sms getItem(int position){
        return smsList.get(position);
    }

    @Override
    public int getItemCount() {
        return smsList.size();
    }

    public void setItems(List<Sms> newSmsList){
        smsList.clear();
        smsList.addAll(newSmsList);
        notifyDataSetChanged();
        scrollToLastMessage();
    }

    public void appendItem(Sms newItem){
        int size = getItemCount();
        smsList.add(newItem);
        notifyItemInserted(size);
        notifyItemChanged(size - 1);
        scrollToLastMessage();
    }

    private void scrollToLastMessage() {
        recyclerView.scrollToPosition(getItemCount() - 1);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chat_message_text_view)
        TextView tvMessageBody;
        @BindView(R.id.timestamp_text_view)
        TextView tvTimestamp;
        @BindView(R.id.chat_bubble)
        FrameLayout chatBubble;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
