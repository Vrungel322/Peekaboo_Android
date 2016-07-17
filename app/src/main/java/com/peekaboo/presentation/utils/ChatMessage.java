package com.peekaboo.presentation.utils;

import android.graphics.Bitmap;

/**
 * Created by Nataliia on 13.07.2016.
 */
public class ChatMessage {
    public boolean right;
    public String message;
    public Bitmap image;

    public ChatMessage(boolean right, String message, Bitmap image) {
        super();
        this.right = right;
        this.message = message;
        this.image = image;

    }

}
