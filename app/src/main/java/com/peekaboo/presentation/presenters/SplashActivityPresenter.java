package com.peekaboo.presentation.presenters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import javax.inject.Inject;

/**
 * Created by Nikita on 13.07.2016.
 */
public class SplashActivityPresenter {

    private SharedPreferences prefs = null;
    private Context mContext;

    @Inject
    public SplashActivityPresenter(Context context, SharedPreferences prefs) {
        mContext = context;
        this.prefs = prefs;
    }

    public boolean isFirstLaunch(){
        return prefs.getBoolean("firstrun", true);
    }

    public void setSecondLaunch(){
        prefs.edit().putBoolean("firstrun", false).apply();
        Log.wtf("prefs", String.valueOf(prefs.getBoolean("firstrun", true)));
    }
}
