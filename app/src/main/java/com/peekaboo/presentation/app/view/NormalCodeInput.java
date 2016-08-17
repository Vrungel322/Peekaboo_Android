package com.peekaboo.presentation.app.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;

import com.github.glomadrian.codeinputlib.CodeInput;

import java.util.Arrays;

/**
 * Created by sebastian on 11.08.16.
 */
public class NormalCodeInput extends CodeInput {
    @Nullable
    private OnCodeChangeListener onCodeChangedListener;

    public NormalCodeInput(Context context) {
        super(context);
    }

    public NormalCodeInput(Context context, AttributeSet attributeset) {
        super(context, attributeset);
    }

    public NormalCodeInput(Context context, AttributeSet attributeset, int defStyledAttrs) {
        super(context, attributeset, defStyledAttrs);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent keyevent) {
        boolean onKeyUp = super.onKeyUp(keyCode, keyevent);
        Log.e("keyUp", String.valueOf(onKeyUp) + " " + Arrays.deepToString(getCode()));
        if (onCodeChangedListener != null) {
            onCodeChangedListener.onCodeChanged(getCode());
        }
        return onKeyUp;
    }

    public void setOnCodeChangedListener(@Nullable OnCodeChangeListener onCodeChangedListener) {
        this.onCodeChangedListener = onCodeChangedListener;
    }

    public interface OnCodeChangeListener {
        void onCodeChanged(Character[] code);
    }
}
