package com.peekaboo.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.peekaboo.R;

import java.util.ArrayList;
import java.util.List;

public final class DialogsLargeAdapter extends RecyclerView.Adapter<DialogsLargeAdapter.ViewHolder>  {
    private final List<String> items;

    public DialogsLargeAdapter(ArrayList<String> item) {
        List<String> items = new ArrayList<>();
        items.addAll(item);
        java.util.Collections.sort(items);
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dialog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String text = items.get(position);
        holder.setText(text);

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        private  TextView dialogname, dialogprew, num_mes, date;
        private ImageView trash, star, sound, contact, status ;

        private ViewHolder(View itemView) {
            super(itemView);
            dialogname = (TextView) itemView.findViewById(R.id.tvDialog_name);
            dialogprew = (TextView) itemView.findViewById(R.id.tvDialog_preview);
            trash = (ImageView) itemView.findViewById(R.id.iv_swipetrash);
            star = (ImageView) itemView.findViewById(R.id.iv_swipestar);
            sound = (ImageView) itemView.findViewById(R.id.iv_swipenosound);
            contact = (ImageView) itemView.findViewById(R.id.ivDialog_contact);
            num_mes = (TextView) itemView.findViewById(R.id.tv_num_mes);
            status = (ImageView) itemView.findViewById(R.id.ivDialog_status);
            date = (TextView) itemView.findViewById(R.id.tvDialog_time_date);

        }

        public void setText(CharSequence text) {
            dialogname.setText(text);
        }
    }
}
