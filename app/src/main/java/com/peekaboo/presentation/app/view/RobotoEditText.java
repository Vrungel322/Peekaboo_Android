package com.peekaboo.presentation.app.view;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.peekaboo.R;
import com.peekaboo.presentation.utils.ResourcesUtils;

/**
 * Created by Nataliia on 01.08.2016.
 */

public class RobotoEditText extends TextInputEditText {
    private int controlNormal;
    private int controlActivated;
    private android.graphics.Typeface roboto;

    public RobotoEditText(Context context) {
        super(context);
        configure(context);
    }

    public RobotoEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        configure(context);
    }
    public RobotoEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        configure(context);
    }
    void configure(Context context) {
        roboto = Typeface.getRoboto(context);
//        setTypeface(roboto);
        controlNormal = ResourcesUtils.getColorFromAttr(context, R.attr.colorControlNormal);
        controlActivated = ResourcesUtils.getColorFromAttr(context, R.attr.colorControlActivated);
        setBackgroundDrawable(ResourcesUtils.getDrawable(context, R.drawable.edit_text_background));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            getBackground().setColorFilter(controlNormal, PorterDuff.Mode.SRC_ATOP);
            setOnFocusChangeListener((v, hasFocus) -> {
                onFocusChanged(hasFocus);
            });
        }
    }

    public void onFocusChanged(boolean hasFocus) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            if (hasFocus) {
                getBackground().setColorFilter(controlActivated, PorterDuff.Mode.SRC_ATOP);
            } else {
                getBackground().setColorFilter(controlNormal, PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    @Override
    public void setInputType(int type) {
        int selectionStart = getSelectionStart();
        super.setInputType(type);
//        setTypeface(roboto);
        setSelection(selectionStart);
    }
}