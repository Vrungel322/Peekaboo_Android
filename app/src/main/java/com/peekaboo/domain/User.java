package com.peekaboo.domain;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

/**
 * Created by sebastian on 28.06.16.
 */
public class User {
    private static final String TOKEN = "token";
    public static final String ID = "user_id";
    private SharedPreferences preferences;

    @Nullable
    private String token;
    @Nullable
    private String id;

    public User(SharedPreferences preferences) {
        this.preferences = preferences;
        restoreData();
    }

    public User(String str) {
        this.id = str;
    }

    @Nullable
    public String getBearer() {
        return "Bearer " + token;
    }

    @Nullable
    public String getToken() {
        return token;
    }

    @Nullable
    public String getId() {
        return id;
    }

    public boolean isAuthorized() {
        return token != null;
    }

    public void saveToken(String token) {
        this.token = token;
        preferences.edit().putString(TOKEN, token).commit();
    }

    public void saveId(String id) {
        this.id = id;
        preferences.edit().putString(ID, id).commit();
    }

    private void restoreData() {
        token = preferences.getString(TOKEN, null);
        id = preferences.getString(ID, null);
    }

    @Override
    public String toString() {
        return "User{" +
                "token='" + token + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
