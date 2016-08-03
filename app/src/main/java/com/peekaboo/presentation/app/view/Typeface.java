package com.peekaboo.presentation.app.view;

import android.content.Context;

import com.peekaboo.R;

/**
 * Created by sebastian on 09.07.16.
 */
public class Typeface {
    private static android.graphics.Typeface helvetica;
    private static android.graphics.Typeface comfortaa_bold;
    private static android.graphics.Typeface roboto;

    public static android.graphics.Typeface getHelvetica(Context context) {
        if (helvetica == null)
            helvetica = android.graphics.Typeface.createFromAsset(context.getAssets(), context.getString(R.string.helvetica_font));
        return helvetica;
    }

    public static android.graphics.Typeface getComfortaa(Context context) {
        if (comfortaa_bold == null)
            comfortaa_bold = android.graphics.Typeface.createFromAsset(context.getAssets(), context.getString(R.string.comfortaa_bold_font));
        return comfortaa_bold;
    }

    public static android.graphics.Typeface getRoboto(Context context) {
        if (roboto == null)
            roboto = android.graphics.Typeface.createFromAsset(context.getAssets(), context.getString(R.string.roboto_font));
        return roboto;
    }
}
