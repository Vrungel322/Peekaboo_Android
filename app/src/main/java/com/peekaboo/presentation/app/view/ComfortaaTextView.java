package com.peekaboo.presentation.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Nataliia on 01.08.2016.
 */

public class ComfortaaTextView extends TextView {
    public ComfortaaTextView(Context context) {
        super(context);
        setTypeface(context);
    }

    public ComfortaaTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(context);
    }


    public ComfortaaTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(context);
    }

    void setTypeface(Context context) {
        setTypeface(Typeface.getComfortaa(context));
    }
}
