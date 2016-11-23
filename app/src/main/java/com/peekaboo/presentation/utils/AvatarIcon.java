package com.peekaboo.presentation.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;

/**
 * Created by Nataliia on 08.11.2016.
 */

public class AvatarIcon {

//    public Drawable createAvatarIcon(Drawable backgroundImage, String contactName, String contactSurname,
//                                     int width, int height) {
//
//        String avatarText;
//
//        if (contactSurname == null) {
//            avatarText = contactName.substring(0,1).toUpperCase();
//        } else {
//            avatarText = contactName.substring(0,1).toUpperCase() + contactSurname.substring(0,1).toUpperCase();
//        }
//
//        backgroundImage = setDrawableColor(backgroundImage, contactName, contactSurname);
//
//        Bitmap canvasBitmap = Bitmap.createBitmap(width, height,
//                Bitmap.Config.ARGB_8888);
//        // Create a canvas, that will draw on to canvasBitmap.
//        Canvas imageCanvas = new Canvas(canvasBitmap);
//
//        // Set up the paint for use with our Canvas
//        Paint imagePaint = new Paint();
//        imagePaint.setTextAlign(Paint.Align.CENTER);
//        imagePaint.setColor(Color.WHITE);
//        imagePaint.setTextSize(62f);
//
//        // Draw the image to our canvas
//        backgroundImage.draw(imageCanvas);
//
//       // Draw the text on top of our image
//        imageCanvas.drawText(avatarText, width / 2, (height * 2) / 3, imagePaint);
//
//
//        // Combine background and text to a LayerDrawable
//        LayerDrawable layerDrawable = new LayerDrawable(
//                new Drawable[]{backgroundImage, new BitmapDrawable(canvasBitmap)});
//        return layerDrawable;
//    }

    public static Drawable setDrawableColor(Drawable background, String contactName, String contactSurname){
        int backgroundColor = Color.rgb(103,58,183);
        switch (contactName.hashCode() % 10){
            case 0:
                backgroundColor = Color.rgb(211,47,47);
                break;
            case 1:
                backgroundColor = Color.rgb(123,31,162);
                break;
            case 2:
                backgroundColor = Color.rgb(48,63,159);
                break;
            case 3:
                backgroundColor = Color.rgb(0,151,167);
                break;
            case 4:
                backgroundColor = Color.rgb(0,121,107);
                break;
            case 5:
                backgroundColor = Color.rgb(56,142,60);
                break;
            case 6:
                backgroundColor = Color.rgb(255,160,0);
                break;
            case 7:
                backgroundColor = Color.rgb(230,74,25);
                break;
            case 8:
                backgroundColor = Color.rgb(194,24,91);
                break;
            case 9:
                backgroundColor = Color.rgb(103,58,183);
                break;
        }

        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(backgroundColor);
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(backgroundColor);
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(backgroundColor);
        }

        return background;
    }
}
