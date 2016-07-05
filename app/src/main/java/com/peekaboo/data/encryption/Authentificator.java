package com.peekaboo.data.encryption;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.peekaboo.domain.Encoder;
import com.peekaboo.domain.User;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sebastian on 01.07.16.
 */
@Singleton
public class Authentificator {

    public static final String PUBLIC_KEY = "public_key";
    private final User user;
    private final SharedPreferences preferences;
    @Nullable
    private String publicKey;
    private String ownPrivateKey;
    private String ownPublicKey;

    @Inject
    public Authentificator(User user, SharedPreferences preferences) {
        this.user = user;
        this.preferences = preferences;
        restoreData();
        generateOwnKeys();
    }

    private void generateOwnKeys() {
        throw new UnsupportedOperationException();
    }

    public String encryptData(String username, String password) {
        throw new UnsupportedOperationException();
    }

    public void savePublicKey(String publicKey) {
        this.publicKey = publicKey;
        preferences.edit().putString(PUBLIC_KEY, Encoder.encode(publicKey)).commit();
    }

    private void restoreData() {
        String encryptedPublicKey = preferences.getString(PUBLIC_KEY, null);
        if (encryptedPublicKey != null) {
            publicKey = Encoder.decode(encryptedPublicKey);
        }
    }


    public boolean hasPublicKey() {
        return publicKey != null;
    }

    public String decryptData(String data) {
        throw new UnsupportedOperationException();
    }


    public User getUser() {
        return user;
    }
}
