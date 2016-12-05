package com.peekaboo.data.rest.entity;

import com.google.gson.annotations.SerializedName;

public class TokenEntity {
    private String token;
    private String id;
    @SerializedName("state")
    private byte mode;
    private String username;
    private String firstName;
    private String lastName;
    private String country;
    private String city;
    private String phone;
    private String email;

    public TokenEntity(String city, String country, String firstName, String id, String lastName, byte mode, String phone, String token, String username, String email) {
        this.city = city;
        this.country = country;
        this.firstName = firstName;
        this.id = id;
        this.lastName = lastName;
        this.mode = mode;
        this.phone = phone;
        this.token = token;
        this.username = username;
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public byte getMode() {
        return mode;
    }

    public String getEmail() {
        return email;
    }
}
