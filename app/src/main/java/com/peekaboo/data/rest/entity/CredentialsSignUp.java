package com.peekaboo.data.rest.entity;

/**
 * Created by Nikita on 08.07.2016.
 */
public class CredentialsSignUp extends Credentials {
    private String username;
    private String phone;
    private String email;

    public CredentialsSignUp(String phone, String username, String email, String password) {
        super(username, password);
        this.phone = phone;
        this.username = username;
        this.email = email;
    }
}
