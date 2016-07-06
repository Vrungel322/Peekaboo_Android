package com.peekaboo.data.rest.entity;

/**
 * Created by sebastian on 06.07.16.
 */
public class Credentials {
    private String login;
    private String password;

    public Credentials(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
