package com.peekaboo.presentation.services;

import android.support.annotation.Nullable;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sebastian on 12.07.16.
 */
public class Message {
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    private Command command;
    private Map<Params, String> params = new HashMap<>();
    @Nullable
    private byte[] body;


    public Message(Command command) {
        this.command = command;
    }

    public Message addParam(Params param, String value) {
        params.put(param, value);
        return this;
    }

    @Nullable
    public byte[] getBody() {
        return body;
    }

    public Message setBody(byte[] body) {
        this.body = body;
        return this;

    }

    @Nullable
    public String getTextBody() {
        if (body != null) {
            try {
                return new String(body, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return new String(body);
            }
        } else {
            return null;
        }
    }

    public Message setTextBody(String body) {
        this.body = body.getBytes(UTF_8);
        return this;
    }

    public Command getCommand() {
        return command;
    }

    public Message setCommand(Command command) {
        this.command = command;
        return this;
    }

    public Map<Params, String> getParams() {
        return params;
    }

    public Message setParams(Map<Params, String> params) {
        for (Map.Entry<Params, String> param : params.entrySet()) {
            addParam(param.getKey(), param.getValue());
        }
        return this;
    }

    @Override
    public String toString() {
        String bodyString = body == null ?
                "null"
                : getTextBody();

        return "Message{" +
                "command=" + command +
                ", params=" + params +
                ", body=" + bodyString +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (command != message.command) return false;
        if (!params.equals(message.params)) return false;
        return Arrays.equals(body, message.body);

    }

    @Override
    public int hashCode() {
        int result = command.hashCode();
        result = 31 * result + params.hashCode();
        result = 31 * result + Arrays.hashCode(body);
        return result;
    }

    public enum Command {MESSAGE, SYSTEMMESSAGE}

    public enum Params {DESTINATION, FROM, TYPE, REASON, DATE, ID}

    public interface Type {
        String TEXT = "text";
        String AUDIO = "audio";
        String IMAGE = "image";
    }

    public interface Reason {
        String END = "end";
        String MODE = "mode";
        String READ = "read";
        String CREATE_DIALOG = "create_dialog";
    }
}
