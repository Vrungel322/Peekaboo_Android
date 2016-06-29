package com.peekaboo.data.mappers;


import com.peekaboo.data.rest.entity.UserEntity;
import com.peekaboo.domain.User;

/**
 * Created by sebastian on 10.06.16.
 */
public interface AbstractMapperFactory {
    Mapper<UserEntity, User> getUserMapper();
}
