package com.peekaboo.domain;

/**
 * Created by sebastian on 28.06.16.
 */
public class User {
    private String id;

    public User(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                '}';
    }
}
