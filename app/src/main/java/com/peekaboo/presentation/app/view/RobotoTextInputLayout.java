package com.peekaboo.presentation.app.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;

/**
 * Created by Nataliia on 01.08.2016.
 */

public class RobotoTextInputLayout extends TextInputLayout {
    public RobotoTextInputLayout(Context context) {
        super(context);
        setTypeface(context);
    }

    public RobotoTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(context);
    }


    public RobotoTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(context);
    }

    void setTypeface(Context context) {
        setTypeface(Typeface.getRoboto(context));
    }

    @Override
    public void setError(@Nullable CharSequence error) {
        super.setError(error);
        if (error == null && getChildAt(0) != null && getChildAt(0) instanceof RobotoEditText) {
            RobotoEditText childAt = (RobotoEditText) getChildAt(0);
            childAt.onFocusChanged(true);
        }
    }
}

