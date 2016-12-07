package com.peekaboo.presentation.app.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.peekaboo.R;
import com.peekaboo.presentation.listeners.TextWatcher;
import com.peekaboo.presentation.utils.ResourcesUtils;

/**
 * Created by sebastian on 01.08.16.
 */
public class PasswordView extends FrameLayout implements RobotoEditText.FocusChangeListener {
    private int TOP_MARGIN_ERROR_VISIBLE;
    private int BOTTOM_MARGIN_ERROR_VISIBLE;
    private int BOTTOM_MARGIN_ERROR_INVISIBLE;
    private RobotoEditText editText;
    private ImageView showView;
    private String hint;
    private TextInputLayout inputLayout;

    public PasswordView(Context context) {
        super(context);
        initializeViews(context);
        handleAttributes(context, null, 0);
    }

    public PasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
        handleAttributes(context, attrs, 0);
    }

    public PasswordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
        handleAttributes(context, attrs, defStyleAttr);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.password_view, this);
        BOTTOM_MARGIN_ERROR_VISIBLE = ResourcesUtils.dpToPx(getContext(), 8);
        TOP_MARGIN_ERROR_VISIBLE = ResourcesUtils.dpToPx(getContext(), 6);
        BOTTOM_MARGIN_ERROR_INVISIBLE = -ResourcesUtils.dpToPx(getContext(), 2);
    }

    private void handleAttributes(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(
                    attrs,
                    R.styleable.PasswordView,
                    defStyleAttr, 0);

            try {
                hint = a.getString(R.styleable.PasswordView_textHint);
            } finally {
                a.recycle();
            }
        }
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        editText.setOnTouchListener(l);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof TextInputLayout) {
                inputLayout = (TextInputLayout) getChildAt(i);
                editText = (RobotoEditText) inputLayout.getChildAt(0);
                editText.setFocusChangeListener(this);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        updateShowViewVisibility(editText.hasFocus());
                    }
                });
                updateEditTextPadding();
            } else {
                showView = (ImageView) getChildAt(i);
            }
        }
//        setOnTouchListener((v, event) -> {
//            Log.e("container", "onTouch " + event.getAction());
//            return false;
//        });
        setHint(hint);
        editText.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        setProperTypeface();
        showView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    editText.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    setProperTypeface();
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    editText.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    setProperTypeface();
                    break;
            }
            return true;
        });
    }

    private void setProperTypeface() {
        editText.setTypeface(android.graphics.Typeface.create("sans-serif", android.graphics.Typeface.NORMAL));
    }

    private void updateEditTextPadding() {
        int paddingTop = editText.getPaddingTop();
        int paddingHorizontal = ResourcesUtils.dpToPx(getContext(), 40);
        int paddingBottom = editText.getPaddingBottom();
        editText.setPadding(paddingHorizontal, paddingTop, paddingHorizontal, paddingBottom);
    }

    public void setHint(String hint) {
        this.hint = hint;
        inputLayout.setHint(hint);
    }

    public void setText(String s) {
        editText.setText(s);
    }

    public void setError(String error) {
        boolean shouldShowError = error != null;
        inputLayout.setErrorEnabled(shouldShowError);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) showView.getLayoutParams();
        layoutParams.setMargins(0, !shouldShowError ? TOP_MARGIN_ERROR_VISIBLE : 0, layoutParams.rightMargin,
                shouldShowError ? BOTTOM_MARGIN_ERROR_VISIBLE : 0);
        inputLayout.setError(error);
        updateEditTextPadding();
    }

    public String getPassword() {
        return editText.getText().toString();
    }

    @Override
    public void onFocusChanged(boolean hasFocus) {
        updateShowViewVisibility(hasFocus);
    }

    private void updateShowViewVisibility(boolean hasFocus) {
        showView.setVisibility(hasFocus && editText.getText().length() > 0 ? VISIBLE : GONE);
    }
}
