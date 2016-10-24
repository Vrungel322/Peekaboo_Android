package com.peekaboo.presentation.animation;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

/**
 * Created by Nikita on 24.10.2016.
 */
public class AnimationForImagesHelper {
    public static void setPreviewImageInChatAnimation(ImageView iv, int duration){
        Animation fadeOut = new AlphaAnimation(0, 1);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(duration);
        iv.startAnimation(fadeOut);
    }
}
