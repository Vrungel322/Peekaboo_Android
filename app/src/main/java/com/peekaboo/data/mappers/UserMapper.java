package com.peekaboo.data.mappers;

import com.peekaboo.data.rest.entity.UserEntity;
import com.peekaboo.domain.User;

/**
 * Created by sebastian on 28.06.16.
 */
public class UserMapper implements Mapper<UserEntity, User> {
    @Override
    public User transform(UserEntity obj) throws RuntimeException {
        return new User(null, null, obj.getRealName(), obj.getId(), obj.getRealSurname(),
                null, obj.getName());
    }
}
