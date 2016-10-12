package com.peekaboo.presentation.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.peekaboo.presentation.utils.ResourcesUtils;

/**
 * Created by Nataliia on 10.10.2016.
 */

public class SmartWidthEditText extends EditText {
    public SmartWidthEditText(Context context) {
        super(context);
        setWidth(context);
    }

    public SmartWidthEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWidth(context);
    }
    public SmartWidthEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWidth(context);
    }

    public void setWidth(Context context){
        setWidth((ResourcesUtils.getDisplayWidth(context) / 100) * 75);
    }

}
