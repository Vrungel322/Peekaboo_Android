package com.peekaboo.presentation.app.view;

import android.content.Context;

import com.peekaboo.R;

/**
 * Created by sebastian on 09.07.16.
 */
public class Typeface {
    private static android.graphics.Typeface helvetica;

    public static android.graphics.Typeface getHelvetica(Context context) {
        if (helvetica == null)
            helvetica = android.graphics.Typeface.createFromAsset(context.getAssets(), context.getString(R.string.helvetica_font));
        return helvetica;
    }
}
