package com.peekaboo.presentation.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;

/**
 * Created by Nataliia on 08.11.2016.
 */

public class AvatarIcon {

    public Drawable createAvatarIcon(Drawable backgroundImage, String text,
                                      int width, int height) {

        backgroundImage = setDrawableColor(backgroundImage);

        Bitmap canvasBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        // Create a canvas, that will draw on to canvasBitmap.
        Canvas imageCanvas = new Canvas(canvasBitmap);

        // Set up the paint for use with our Canvas
        Paint imagePaint = new Paint();
        imagePaint.setTextAlign(Paint.Align.CENTER);
        imagePaint.setColor(Color.WHITE);
        imagePaint.setTextSize(64f);

        // Draw the image to our canvas
        backgroundImage.draw(imageCanvas);

        // Draw the text on top of our image
        imageCanvas.drawText(text, width / 2, 2 *height / 3 , imagePaint);

        // Combine background and text to a LayerDrawable
        LayerDrawable layerDrawable = new LayerDrawable(
                new Drawable[]{backgroundImage, new BitmapDrawable(canvasBitmap)});
        return layerDrawable;
    }

    private Drawable setDrawableColor(Drawable drawable){

        ((GradientDrawable)drawable).setColor(Color.rgb(103,58,183));
        return drawable;
    }
}
