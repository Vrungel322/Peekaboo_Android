package com.peekaboo.presentation.presenters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import javax.inject.Inject;

/**
 * Created by Nikita on 28.06.2016.
 */
public class MainActivityPresenter {

    private SharedPreferences prefs = null;
    private Context mContext;

    @Inject
    public MainActivityPresenter(Context context) {
        mContext = context;
    }

    public void createSharedPref(){
        prefs = mContext.getSharedPreferences("com.peekaboo.Peekaboo", mContext.MODE_PRIVATE);
    }

    public boolean isFirstLaunch(){
        return prefs.getBoolean("firstrun", true);
    }
    public void setSecondLaunch(){
        prefs.edit().putBoolean("firstrun", false).apply();
        Log.wtf("prefs", String.valueOf(prefs.getBoolean("firstrun", true)));
    }
}
