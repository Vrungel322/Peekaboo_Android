package com.peekaboo.presentation.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by arkadii on 11/1/16.
 */

public class ActivityUtils {
    public static void hideKeyboard(Activity activity) {

        View view = activity.getWindow().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }
}
