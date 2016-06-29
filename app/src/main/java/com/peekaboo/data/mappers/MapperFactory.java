package com.peekaboo.data.mappers;

import com.peekaboo.data.rest.entity.UserEntity;
import com.peekaboo.domain.User;

/**
 * Created by sebastian on 28.06.16.
 */
public class MapperFactory implements AbstractMapperFactory {
    @Override
    public Mapper<UserEntity, User> getUserMapper() {
        return new UserMapper();
    }
}
