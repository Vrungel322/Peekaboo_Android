package com.peekaboo.domain;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by sebastian on 28.06.16.
 */
public class AccountUser extends User {
    public static final String TOKEN = "token";
    public static final String ID = "user_id";
    public static final String MODE = "mode";
    public static final String USERNAME = "username";
    public static final String NOTIFICATIONS_ENABLED = "notifications_enabled";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String CITY = "city";
    public static final String COUNTRY = "country";
    public static final String PHONE = "phone";
    public static final String EMAIL = "email";
    private SharedPreferences preferences;
    private String domen;
    @Nullable
    private String token;
    private byte mode;
    @Nullable
    private String username;
    private boolean notificationsEnabled;
    @Nullable
    private String firstName;
    @Nullable
    private String lastName;
    @Nullable
    private String country;
    @Nullable
    private String city;
    @Nullable
    private String phone;
    @Nullable
    private String email;

    public AccountUser(SharedPreferences preferences, String domen) {
        super(null, null, null, null, null, null, null, null);
        this.preferences = preferences;
        this.domen = domen;
        restoreData();
    }

    public AccountUser(String id) {
        super(id, null, null, null, null, null, null, null);
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    public void saveEmail(String email){
        this.email = email;
        preferences.edit().putString(EMAIL, email).apply();
    }

    @Nullable
    public String getCountry() {
        return country;
    }

    public void saveCountry(String country) {
        this.country = country;
        preferences.edit().putString(COUNTRY, country).apply();
    }

    @Nullable
    public String getCity() {
        return city;
    }

    public void saveCity(String city) {
        this.city = city;
        preferences.edit().putString(CITY, city).apply();
    }

    @Nullable
    public String getFirstName() {
        return firstName;
    }

    public void saveFirstName(String firstName) {
        this.firstName = firstName;
        preferences.edit().putString(FIRST_NAME, firstName).apply();
    }

    @Nullable
    public String getLastName() {
        return lastName;
    }

    public void saveLastName(String lastName) {
        this.lastName = lastName;
        preferences.edit().putString(LAST_NAME, lastName).apply();
    }

    @Nullable
    public String getPhone() {
        return phone;
    }

    public void savePhone(String phone) {
        this.phone = phone;
        preferences.edit().putString(PHONE, phone).apply();
    }

    @Nullable
    public String getUsername() {
        return username;
    }

    public void saveUsername(String username) {
        this.username = username;
        preferences.edit().putString(USERNAME, username).apply();
    }

    @Nullable
    public String getBearer() {
        return "Bearer " + token;
    }


    public String getAvatar() {
        return domen + getId();
    }


    public String getAvatarMiddle() {
        return getAvatar() + "";
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
        preferences.edit().putString(TOKEN, token).apply();
    }

    public void saveMode(byte mode) {
        this.mode = mode;
        preferences.edit().putInt(MODE, mode).apply();
    }

    public void saveId(String id) {
        setId(id);
        preferences.edit().putString(ID, id).apply();
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
        firstName = preferences.getString(FIRST_NAME, null);
        lastName = preferences.getString(LAST_NAME, null);
        country = preferences.getString(COUNTRY, null);
        city = preferences.getString(CITY, null);
        phone = preferences.getString(PHONE, null);
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
        Log.e("AccountUser", "notifications enabled " + notificationsEnabled);
        this.notificationsEnabled = notificationsEnabled;
        preferences.edit().putBoolean(NOTIFICATIONS_ENABLED, notificationsEnabled).commit();
    }

    public boolean notificationsEnabled() {
        return notificationsEnabled;
    }
}
