package com.peekaboo.presentation.utils;

/**
 * Created by sebastian on 04.07.16.
 */
public class CredentialUtils {
    public static boolean isUsernameValid(String login) {
        return !(login.contains(" ") || login.length() < 5);
    }

    public static boolean isPasswordValid(String password) {
        return !(password.contains(" ") || password.length() < 6);
    }

    public static boolean isEmailValid(String email) {
        return !email.contains(" ") && email.contains("@");
    }

    public static boolean isPasswordConfirmed(String password, String passwordConfirm) {
        return password.equals(passwordConfirm);
    }

    public static boolean isPhoneNumberValid(String login) {
        return false;
    }
}
