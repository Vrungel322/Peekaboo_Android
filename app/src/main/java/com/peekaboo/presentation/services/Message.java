package com.peekaboo.presentation.services;

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
    private byte[] body;


    public Message(Command command) {
        this.command = command;
    }

    public void addParam(Params param, String value) {
        params.put(param, value);
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void setTextBody(String body) {
        this.body = body.getBytes(UTF_8);
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public Map<Params, String> getParams() {
        return params;
    }

    public void setParams(Map<Params, String> params) {
        for (Map.Entry<Params, String> param : params.entrySet()) {
            addParam(param.getKey(), param.getValue());
        }
    }

    @Override
    public String toString() {
        return "Message{" +
                "command=" + command +
                ", params=" + params +
                ", body=" + Arrays.toString(body) +
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

    public enum Command {ACCEPT, CALL, REJECT, SEND, MESSAGE, NONE}

    public enum Params {DESTINATION, FROM, TYPE, REASON, DATE}
}
