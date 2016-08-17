package com.peekaboo.presentation.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;

public class ResourcesUtils {

    public static Drawable getDrawable(Context context, int resId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return context.getResources().getDrawable(resId);
        } else {
            return context.getDrawable(resId);
        }
    }

    public static int getColorFromAttr(Context context, int attrId) {
        int[] attribute = new int[] { attrId };
        TypedArray array = context.getTheme().obtainStyledAttributes(attribute);
        int color = array.getColor(0, Color.RED);
        array.recycle();
        return color;
    }

    public static int getColor(Context c, int resourceId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return c.getColor(resourceId);
        } else {
            return c.getResources().getColor(resourceId);
        }
    }

    public static int dpToPx(Context c, int dp) {
        return (int) (c.getResources().getDisplayMetrics().density * dp);
    }

    public static ColorStateList getColorSateList(Context context, int resId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return context.getResources().getColorStateList(resId);
        } else {
            return context.getResources().getColorStateList(resId, context.getTheme());
        }
    }

}
