package com.peekaboo.data.rest.entity;

/**
 * Created by Arkadiy on 02.06.2016.
 */
public class UserEntity {
    private String id;
    private String name;
    private String realName;
    private String realSurname;


    public UserEntity(String id, String name, String realName, String realSurname) {
        this.id = id;
        this.name = name;
        this.realName = realName;
        this.realSurname = realSurname;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getRealName() {
        return realName;
    }

    public String getRealSurname() {
        return realSurname;
    }
}
