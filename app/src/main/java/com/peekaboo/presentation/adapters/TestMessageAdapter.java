package com.peekaboo.presentation.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.peekaboo.R;
import com.peekaboo.data.repositories.database.messages.PMessageAbs;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by sebastian on 22.08.16.
 */
public class TestMessageAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private List<PMessageAbs> messages = new ArrayList<>();

    public TestMessageAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void addItem(PMessageAbs item) {
        messages.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public PMessageAbs getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PMessageAbs item = getItem(position);

        TextView view = (TextView)inflater.inflate(R.layout.test_list_item, parent, false);
        view.setText(item.status() + " " + item.messageBody());
        view.setGravity(item.isMine() ? Gravity.RIGHT : Gravity.LEFT);

        return view;
    }
}
