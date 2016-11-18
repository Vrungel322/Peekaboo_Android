package com.peekaboo.presentation.app.view;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import com.squareup.picasso.Transformation;

/**
 * Created by Nataliia on 12.08.2016.
 */

public class RoundedTransformation implements Transformation{
    private final int radius;
    private final int margin;  // dp

    // radius is corner radii in dp
    // margin is the board in dp
    public RoundedTransformation(final int radius, final int margin) {
        this.radius = radius;
        this.margin = margin;
    }

    @Override
    public Bitmap transform(final Bitmap source) {
//        int size = Math.min(source.getWidth(), source.getHeight());

//        int x = (source.getWidth() - size) / 2;
//        int y = (source.getHeight() - size) / 2;

//        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
//        if (squaredBitmap != source) {
//            source.recycle();
//        }

        Bitmap bitmap = Bitmap.createBitmap(source.getWidth(), source.getHeight(), source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(source,
                BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

//        float r = size / 2f;
        float r = radius;
        canvas.drawRoundRect(new RectF(0, 0, source.getWidth(), source.getHeight()), r, r, paint);
//        canvas.drawCircle(r, r, r, paint);

        source.recycle();
        return bitmap;
//        final Paint paint = new Paint();
//        paint.setAntiAlias(true);
//        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
//
//        Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(output);
//        canvas.drawRoundRect(new RectF(margin, margin, source.getWidth() - margin, source.getHeight() - margin), radius, radius, paint);
//        if (source != output) {
//            source.recycle();
//        }
//
//        return output;
    }

    @Override
    public String key() {
        return "rounded";
    }
}