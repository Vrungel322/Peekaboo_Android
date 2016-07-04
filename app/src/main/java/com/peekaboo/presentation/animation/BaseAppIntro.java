package com.peekaboo.presentation.animation;

import android.view.MenuItem;

import com.github.paolorotolo.appintro.AppIntro;

/**
 * Created by julio on 20/10/15.
 */
public abstract class BaseAppIntro extends AppIntro {
    private int mScrollDurationFactor = 1;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setScrollDurationFactor(mScrollDurationFactor);
        return super.onOptionsItemSelected(item);
    }
}
