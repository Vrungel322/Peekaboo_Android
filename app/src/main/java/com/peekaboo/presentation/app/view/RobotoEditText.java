package com.peekaboo.presentation.app.view;

import android.content.Context;
import android.graphics.*;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;

import com.peekaboo.R;

/**
 * Created by Nataliia on 01.08.2016.
 */

public class RobotoEditText extends TextInputEditText {
    public RobotoEditText(Context context) {
        super(context);
        setTypeface(context);
    }

    public RobotoEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(context);
    }
    public RobotoEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(context);
    }
    void setTypeface(Context context) {
        setTypeface(Typeface.getRoboto(context));
    }

}