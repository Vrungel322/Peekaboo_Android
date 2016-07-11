package com.peekaboo.presentation.app.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;

import com.peekaboo.R;

/**
 * Created by sebastian on 09.07.16.
 */
public class HelveticaEditText extends TextInputEditText {
    public HelveticaEditText(Context context) {
        super(context);
        setTypeface(context);
    }

    public HelveticaEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(context);
    }


    public HelveticaEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(context);
    }
    void setTypeface(Context context) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.helvetica_font));
        setTypeface(font);
    }
}
