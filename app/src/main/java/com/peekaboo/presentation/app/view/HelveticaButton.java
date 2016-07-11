package com.peekaboo.presentation.app.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.widget.Button;

import com.peekaboo.R;

/**
 * Created by sebastian on 09.07.16.
 */
public class HelveticaButton extends AppCompatButton {
    public HelveticaButton(Context context) {
        super(context);
        setTypeface(context);
    }

    public HelveticaButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(context);
    }

    public HelveticaButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(context);
    }

    void setTypeface(Context context) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.helvetica_font));
        setTypeface(font);
    }


}
