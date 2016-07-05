package com.peekaboo.domain;

import android.util.Base64;

/**
 * Created by sebastian on 01.07.16.
 */
public class Encoder {

    public static String encode(String input) {
        // Simple encryption, not very strong!
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

    public static String decode(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }
}
