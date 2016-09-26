package com.peekaboo.data.rest.entity;

import com.google.gson.annotations.SerializedName;

public class TokenEntity {
    private String token;
    private String id;
    @SerializedName("state")
    private int mode;
    private String username;


    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }


    public int getMode() {
        return mode;
    }

    public TokenEntity(String token, String id) {
        this.token = token;
        this.id = id;
    }
}
