package com.peekaboo.presentation.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.peekaboo.domain.Sms;
import com.peekaboo.presentation.fragments.PeekabooDialogsFragment;
import com.peekaboo.presentation.fragments.SmsDialogsFragment;

/**
 * Created by Nataliia on 28.10.2016.
 */

public class TabsAdapter extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    public TabsAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                PeekabooDialogsFragment tab1 = new PeekabooDialogsFragment();
                return tab1;
            case 1:
                SmsDialogsFragment tab2 = new SmsDialogsFragment();
                return tab2;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return tabCount;
    }
}
