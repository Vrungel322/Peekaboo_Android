package com.peekaboo.data.rest.entity;

/**
 * Created by Nikita on 08.07.2016.
 */
public class CredentialsSignUp extends Credentials {
    private String username;
    private String phone;

    public CredentialsSignUp(String phone, String username, String login, String password) {
        super(login, password);
        this.phone = phone;
        this.username = username;
    }
}
