package com.peekaboo.presentation.app.view;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by sebastian on 09.07.16.
 */
public class HelveticaTextInputLayout extends TextInputLayout {
    public HelveticaTextInputLayout(Context context) {
        super(context);
        setTypeface(context);
    }

    public HelveticaTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(context);
    }


    public HelveticaTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(context);
    }

    void setTypeface(Context context) {
        setTypeface(Typeface.getHelvetica(context));
    }

}
