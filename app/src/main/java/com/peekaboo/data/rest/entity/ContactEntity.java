package com.peekaboo.data.rest.entity;

import com.peekaboo.domain.SessionRepository;

/**
 * Created by Nikita on 13.09.2016.
 */
public class ContactEntity {

    private Long id;
    private String username;
    private String name;
    private int state;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }


    public String getName() {
        return name;
    }

    public int getState() {
        return state;
    }
}
