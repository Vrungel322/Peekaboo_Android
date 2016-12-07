package com.peekaboo.domain;

import android.support.annotation.Nullable;

/**
 * Created by sebastian on 12.08.16.
 */
public class User {
    private String id;
    private String username;
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

    public User(@Nullable String city, @Nullable String country, @Nullable String firstName,
                String id, @Nullable String lastName, @Nullable String phone, String username, String email) {
        this.city = city;
        this.country = country;
        this.firstName = firstName;
        this.id = id;
        this.lastName = lastName;
        this.phone = phone;
        this.username = username;
        this.email = email;
    }

    @Nullable
    public String getCity() {
        return city;
    }

    public void setCity(@Nullable String city) {
        this.city = city;
    }

    @Nullable
    public String getCountry() {
        return country;
    }

    public void setCountry(@Nullable String country) {
        this.country = country;
    }

    @Nullable
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(@Nullable String firstName) {
        this.firstName = firstName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Nullable
    public String getLastName() {
        return lastName;
    }

    public void setLastName(@Nullable String lastName) {
        this.lastName = lastName;
    }

    @Nullable
    public String getPhone() {
        return phone;
    }

    public void setPhone(@Nullable String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + username + '\'' +
                '}';
    }
}
