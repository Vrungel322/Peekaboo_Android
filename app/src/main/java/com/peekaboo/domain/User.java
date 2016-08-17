package com.peekaboo.domain;

/**
 * Created by sebastian on 12.08.16.
 */
public class User {
    private String id;

    public User(String id) {
        this.id = id;
    }



    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
