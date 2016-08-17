package com.peekaboo.data;

/**
 * Created by sebastian on 12.08.16.
 */
public class FileEntity {
    private String result;
    private String name;

    public String getResult() {
        return result;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "FileEntity{" +
                "result='" + result + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
