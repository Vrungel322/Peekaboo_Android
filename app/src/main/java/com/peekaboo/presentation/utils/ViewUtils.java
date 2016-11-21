package com.peekaboo.presentation.utils;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Nikita on 22.11.2016.
 */

public class ViewUtils {

    public static void changeSearchViewTextColor(View view) {
        if (view != null) {
            if (view instanceof EditText) {
                ((TextView) view).setTextColor(Color.BLACK);
                return;
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    changeSearchViewTextColor(viewGroup.getChildAt(i));
                }
            }
        }
    }

}
