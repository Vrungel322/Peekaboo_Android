package com.peekaboo.domain;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

/**
 * Created by sebastian on 28.06.16.
 */
public class AccountUser extends User {
    public static final String TOKEN = "token";
    public static final String ID = "user_id";
    private SharedPreferences preferences;

    @Nullable
    private String token;

    public AccountUser(SharedPreferences preferences) {
        super(null);
        this.preferences = preferences;
        restoreData();
    }

    public AccountUser(String str) {
        super(str);
    }

    @Nullable
    public String getBearer() {
        return "Bearer " + token;
    }

    @Nullable
    public String getToken() {
        return token;
    }

    public boolean isAuthorized() {
        return token != null && getId() != null;
    }

    public void saveToken(String token) {
        this.token = token;
        preferences.edit().putString(TOKEN, token).commit();
    }

    public void saveId(String id) {
        setId(id);
        preferences.edit().putString(ID, id).commit();
    }

    private void restoreData() {
        token = preferences.getString(TOKEN, null);
        setId(preferences.getString(ID, null));
    }

    @Override
    public String toString() {
        return "AccountUser{" +
                "token='" + token + '\'' +
                ", id='" + getId() + '\'' +
                '}';
    }
}
