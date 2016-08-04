package com.peekaboo.presentation.app.view;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.peekaboo.R;

/**
 * Created by sebastian on 01.08.16.
 */
public class PasswordView extends FrameLayout {
    private EditText editText;
    private ImageView showView;

    public PasswordView(Context context) {
        super(context);
        initializeViews(context);
    }

    public PasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public PasswordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.password_view, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof TextInputLayout) {
                editText = (EditText) ((TextInputLayout) getChildAt(i)).getChildAt(0);
            } else {
                showView = (ImageView) getChildAt(i);
            }
        }
//        setOnTouchListener((v, event) -> {
//            Log.e("container", "onTouch " + event.getAction());
//            return false;
//        });
        showView.setOnTouchListener((v, event) -> {
            Log.e("action", String.valueOf(event.getAction()));
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:

                    editText.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);

                    break;
            }
            return true;
        });
    }
}
