package com.peekaboo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Created by sebastian on 12.07.16.
 */
public class ImageUtils {
//    public static Bitmap getScaledBitmap(Uri data, int maxSize) {
//        return decodeSampledBitmapFromResource(new File(data.), maxSize);
//    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(String file, int maxSize) {

        final BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, options);
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        Log.e("photo", String.format("was:%d*%d", outWidth, outHeight));
        Log.e("photo", String.format("maxSize:%d", maxSize));

        options.inSampleSize = calculateInSampleSize(options, maxSize, maxSize);
        Log.e("photo", String.format("inSampleSize:%d", options.inSampleSize));
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(file, options);
        Log.e("photo", String.format("now:%d*%d", bitmap.getWidth(), bitmap.getHeight()));

        return bitmap;
    }

    public static Bitmap resize(Bitmap data, int maxSize, boolean recycle) {
        Bitmap result = Bitmap.createScaledBitmap(data, maxSize, maxSize, false);
        Log.e("photo", String.format("was:%d*%d now:%d*%d", data.getWidth(), data.getHeight(), result.getWidth(), result.getHeight()));
        if (recycle) {
            data.recycle();
        }

        return result;
    }
}
