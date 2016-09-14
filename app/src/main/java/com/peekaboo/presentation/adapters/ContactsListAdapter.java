package com.peekaboo.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.peekaboo.R;

import java.util.ArrayList;

public class ContactsListAdapter extends BaseAdapter implements SectionIndexer {

    private String mSections = "*ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private ArrayList<String> mCurrentList;
    private Context mContext;

    public ContactsListAdapter(ArrayList<String> mCurrentList, Context mContext) {
        super();
        this.mCurrentList = mCurrentList;
        this.mContext = mContext;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void onItemSelected(int position) {
        Toast.makeText(mContext, "ska", Toast.LENGTH_SHORT).show();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int p = position;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.img_row_layout, null);

        try {
            TextView textRow = (TextView) convertView.findViewById(R.id.name);
            textRow.setText(getItem(position));
        } catch (Exception e) {}

        return convertView;
    }

    @Override
    public int getPositionForSection(int section) {
        for (int j = 0; j < getCount(); j++) {
            if (section == 0) {
                // For numeric section
                for (int k = 0; k <= 9; k++) {
                    String text = null;
                    try {
                        text = mCurrentList.get(j);
                    } catch (Exception e) {
                    }
                    if (text == null)
                        return 0;
                    else if (String.valueOf(text.charAt(0)).toLowerCase().equals(String.valueOf(String.valueOf(k)).toString().toLowerCase()))
                        return j;
                }
            } else {
                String artist = null;
                try {
                    artist = mCurrentList.get(j);
                } catch (Exception e) {
                }
                if (artist == null)
                    return 0;
                else if (String.valueOf(artist.charAt(0)).toLowerCase().equals(String.valueOf(mSections.charAt(section)).toString().toLowerCase())) {
                    return j;
                }
            }
        }
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    @Override
    public Object[] getSections() {
        // return content in side view
        String[] sections = new String[mSections.length()];
        for (int i = 0; i < mSections.length(); i++)
            sections[i] = String.valueOf(mSections.charAt(i));
        return sections;
    }

    @Override
    public int getCount() {
        if (mCurrentList != null)
            return mCurrentList.size();
        else
            return 0;
    }

    @Override
    public String getItem(int position) {
        if (mCurrentList != null)
            return mCurrentList.get(position);
        else
            return null;
    }
}