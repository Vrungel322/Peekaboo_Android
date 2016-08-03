package com.peekaboo.presentation.app.view;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * Created by Nataliia on 01.08.2016.
 */
public class ComfortaaButton extends AppCompatButton {
    public ComfortaaButton(Context context) {
        super(context);
        setTypeface(context);
    }

    public ComfortaaButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(context);
    }

    public ComfortaaButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(context);
    }

    void setTypeface(Context context) {
        //android.graphics.Typeface font = android.graphics.Typeface.createFromAsset(context.getAssets(), context.getString(R.string.comfortaa_bold_font));
        setTypeface(Typeface.getComfortaa(context));
    }


}