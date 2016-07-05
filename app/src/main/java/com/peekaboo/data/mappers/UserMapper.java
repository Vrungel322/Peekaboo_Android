package com.peekaboo.data.mappers;

import com.peekaboo.data.rest.entity.UserEntity;
import com.peekaboo.domain.User;

/**
 * Created by sebastian on 28.06.16.
 */
public class UserMapper implements Mapper<String, User> {
    @Override
    public User transform(String obj) throws RuntimeException {
        return new User(obj);
    }
}
