package com.peekaboo.presentation.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

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

    public static int dpToPx(Context c, float dp) {
        float density = c.getResources().getDisplayMetrics().density;
        return (int) (density * dp);
    }

    public static ColorStateList getColorSateList(Context context, int resId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return context.getResources().getColorStateList(resId);
        } else {
            return context.getResources().getColorStateList(resId, context.getTheme());
        }
    }

    public static int getDimenInPx(Context c, int resId) {
        return (int) c.getResources().getDimension(resId);
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
