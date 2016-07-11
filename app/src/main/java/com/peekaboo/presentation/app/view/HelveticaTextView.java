package com.peekaboo.presentation.app.view;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;
import android.widget.TextView;

import com.peekaboo.R;

/**
 * Created by sebastian on 09.07.16.
 */
public class HelveticaTextView extends TextView {
    public HelveticaTextView(Context context) {
        super(context);
        setTypeface(context);
    }

    public HelveticaTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(context);
    }


    public HelveticaTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(context);
    }

    void setTypeface(Context context) {
        setTypeface(Typeface.getHelvetica(context));
    }
}
