package com.peekaboo.data.rest.entity;

/**
 * Created by Nikita on 13.09.2016.
 */
public class ContactEntity {

    private String id;
    private String name;
    private String surname;
    private String nickname;
    private int state;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public int getState() {
        return state;
    }

    public String getSurname() {
        return surname;
    }
}
