package com.peekaboo.domain;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

/**
 * Created by sebastian on 28.06.16.
 */
public class AccountUser extends User {
    public static final String TOKEN = "token";
    public static final String ID = "user_id";
    public static final String MODE = "mode";
    public static final String USERNAME = "username";
    public static final String NOTIFICATIONS_ENABLED = "notifications_enabled";
    private SharedPreferences preferences;
    private String domen;
    @Nullable
    private String token;
    private byte mode;
    @Nullable
    private String username;
    private boolean notificationsEnabled;

    public AccountUser(SharedPreferences preferences, String domen) {
        super(null, null);
        this.preferences = preferences;
        this.domen = domen;
        restoreData();
    }

    public AccountUser(String id) {
        super(id, null);
    }

    @Nullable
    public String getUsername() {
        return username;
    }

    public void saveUsername(String username) {
        this.username = username;
        preferences.edit().putString(USERNAME, username).commit();
    }

    @Nullable
    public String getBearer() {
        return "Bearer " + token;
    }


    public String getAvatar() {
        return domen + getId();
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

    public void saveMode(byte mode) {
        this.mode = mode;
        preferences.edit().putInt(MODE, mode).commit();
    }

    public void saveId(String id) {
        setId(id);
        preferences.edit().putString(ID, id).commit();
    }

    public byte getMode() {
        return mode;
    }

    private void restoreData() {
        token = preferences.getString(TOKEN, null);
        setId(preferences.getString(ID, null));
        mode = (byte) preferences.getInt(MODE, 0);
        username = preferences.getString(USERNAME, null);
        notificationsEnabled = preferences.getBoolean(NOTIFICATIONS_ENABLED, true);
    }

    public void logout() {
        token = null;
        mode = 0;
        username = null;
        setId(null);
        notificationsEnabled = true;
        preferences.edit().clear().commit();
    }
    @Override
    public String toString() {
        return "AccountUser{" +
                "token='" + token + '\'' +
                ", id='" + getId() + '\'' +
                '}';
    }

    public void saveNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
        preferences.edit().putBoolean(NOTIFICATIONS_ENABLED, notificationsEnabled).commit();
    }

    public boolean notificationsEnabled() {
        return notificationsEnabled;
    }
}
