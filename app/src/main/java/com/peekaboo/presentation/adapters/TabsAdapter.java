package com.peekaboo.presentation.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.peekaboo.R;
import com.peekaboo.domain.Sms;
import com.peekaboo.presentation.fragments.PeekabooDialogsFragment;
import com.peekaboo.presentation.fragments.SmsDialogsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nataliia on 28.10.2016.
 */

public class TabsAdapter extends FragmentStatePagerAdapter {

    public static final int FRAGMENT_COUNT = 2;
    private final String[] tabNames;
    //integer to count number of tabs
    private final List<Fragment> fragments = new ArrayList<>();

    public TabsAdapter(FragmentManager fm, Context context) {
        super(fm);
        tabNames = context.getResources().getStringArray(R.array.dialog_tab_names);
        //Initializing tab count
        fragments.add(new PeekabooDialogsFragment());
        fragments.add(new SmsDialogsFragment());

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabNames[position];
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        return fragments.get(position);
    }
    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }
}
