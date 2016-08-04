package com.peekaboo.presentation.app.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;
import android.util.Log;
import android.util.StateSet;
import android.view.View;

import com.peekaboo.R;
import com.peekaboo.presentation.utils.ResourcesUtils;

/**
 * Created by sebastian on 09.07.16.
 */
public class HelveticaEditText extends TextInputEditText {

    private int controlNormal;
    private int controlActivated;
    private Typeface typeface;

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
        controlNormal = ResourcesUtils.getColorFromAttr(context, R.attr.colorControlNormal);
        controlActivated = ResourcesUtils.getColorFromAttr(context, R.attr.colorControlActivated);
        typeface = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.helvetica_font));
        setTypeface(typeface);
        setBackgroundDrawable(ResourcesUtils.getDrawable(context, R.drawable.edit_text_background));
//        StateListDrawable stateListDrawable = new StateListDrawable();
//        Drawable drawable = ResourcesUtils.getDrawable(context, R.drawable.abc_textfield_default_mtrl_alpha);
//        drawable.setColorFilter(controlNormal, PorterDuff.Mode.SRC_ATOP);
//        Drawable drawableFocused = ResourcesUtils.getDrawable(context, R.drawable.abc_textfield_default_mtrl_alpha);
//        drawableFocused.setColorFilter(controlActivated, PorterDuff.Mode.SRC_ATOP);
//
//        int insetHorizontal = ResourcesUtils.dpToPx(context, 4);
//        int insetTop = ResourcesUtils.dpToPx(context, 10);
//        int insetBottom = ResourcesUtils.dpToPx(context, 7);
//
//        InsetDrawable insetDrawable = new InsetDrawable(drawable, insetHorizontal, insetTop, insetHorizontal, insetBottom);
//        InsetDrawable insetDrawableFocused = new InsetDrawable(drawableFocused, insetHorizontal, insetTop, insetHorizontal, insetBottom);
//        stateListDrawable.addState(new int[] {android.R.attr.state_focused}, insetDrawableFocused);
//        stateListDrawable.addState(StateSet.WILD_CARD, insetDrawable);
//        setBackgroundDrawable(stateListDrawable);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            getBackground().setColorFilter(controlNormal, PorterDuff.Mode.SRC_ATOP);
            setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    getBackground().setColorFilter(controlActivated, PorterDuff.Mode.SRC_ATOP);
                } else {
                    getBackground().setColorFilter(controlNormal, PorterDuff.Mode.SRC_ATOP);
                }
            });
        }
    }

    @Override
    public void setInputType(int type) {
        super.setInputType(type);
        setTypeface(typeface);
    }
}
