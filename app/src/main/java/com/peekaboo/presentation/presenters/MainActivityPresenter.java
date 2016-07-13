package com.peekaboo.presentation.presenters;

import android.content.Context;

import javax.inject.Inject;

/**
 * Created by Nikita on 28.06.2016.
 */
public class MainActivityPresenter {

    private Context mContext;

    @Inject
    public MainActivityPresenter(Context context) {
        mContext = context;
    }


}
