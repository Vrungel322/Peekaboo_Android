package com.peekaboo.domain;

/**
 * Created by sebastian on 12.08.16.
 */
public class User {
    private String id;
    private String avatar;

    public User(String id, String avatar) {
        this.id = id;
        this.avatar = avatar;
    }



    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getAvatar() {
        return avatar;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
