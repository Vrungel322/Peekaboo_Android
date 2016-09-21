package com.peekaboo.domain;

/**
 * Created by sebastian on 05.07.16.
 */
public interface UserMessageMapper {
    String handleError(Throwable t);
    String getMessageFromResource(int stringId);
}
