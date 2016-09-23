package com.peekaboo.data.rest.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arkadiy on 02.06.2016.
 */
public class UserEntity {
    private String id;
    private String name;




    public UserEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
