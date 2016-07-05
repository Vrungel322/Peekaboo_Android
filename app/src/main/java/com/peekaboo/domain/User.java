package com.peekaboo.domain;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sebastian on 28.06.16.
 */
public class User {
    private static final String BEARER = "bearer";
    private SharedPreferences preferences;

    @Nullable
    private String bearer;

    public User(SharedPreferences preferences) {
        this.preferences = preferences;
        restoreData();
    }

    public User(String str) {

    }

    @Nullable
    public String getBearer() {
        return "Bearer " + bearer;
    }

    public boolean isAuthorized() {
        return bearer != null;
    }

    public void saveBearer(String bearer) {
        this.bearer = bearer;
        preferences.edit().putString(BEARER, bearer).commit();
    }

    private void restoreData() {
        bearer = preferences.getString(BEARER, null);
    }

    @Override
    public String toString() {
        return "User{" +
                "bearer='" + bearer + '\'' +
                '}';
    }
}
