package com.peekaboo.presentation.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by sebastian on 04.07.16.
 */
public class CredentialUtils {
    public static String getPhoneNumber(Context context){
        TelephonyManager tMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return tMgr.getLine1Number().replace("+", "");
    }

    public static boolean isUsernameValid(String login) {
        return !(login.contains(" ") || login.length() < 5);
    }

    public static boolean isPasswordValid(String password) {
        return !(password.contains(" ") || password.length() < 6);
    }

    public static boolean isEmailValid(String email) {
//        return !email.contains(" ") && email.contains("@");
        return true;
    }

    public static boolean isPasswordConfirmed(String password, String passwordConfirm) {
        return password.equals(passwordConfirm);
    }

    public static boolean isPhoneNumberValid(String login) {
        return false;
    }
}
