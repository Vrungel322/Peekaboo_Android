package com.peekaboo.domain;

/**
 * Created by sebastian on 05.07.16.
 */
public class Error {
    private String type;
    private String message;

    public Error(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
