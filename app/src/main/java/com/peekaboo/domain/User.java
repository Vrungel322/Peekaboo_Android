package com.peekaboo.domain;

/**
 * Created by sebastian on 12.08.16.
 */
public class User {
    private String id;
    private String name;
    private String realName;
    private String realSurname;

    public User(String id, String name, String realName, String realSurname) {
        this.id = id;
        this.name = name;
        this.realName = realName;
        this.realSurname = realSurname;
    }

    public String getRealName() {
        return realName;
    }

    public String getRealSurname() {
        return realSurname;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
