package com.peekaboo.data.mappers;

import com.peekaboo.domain.AccountUser;
import com.peekaboo.domain.User;

/**
 * Created by Nikita on 02.12.2016.
 */

public class AccoutUSerToUserMapper implements Mapper<AccountUser, User> {
    @Override
    public User transform(AccountUser obj) throws RuntimeException {
        return new User(obj.getCity(),
                obj.getCountry(),obj.getFirstName(), obj.getId(),
                obj.getLastName(), obj.getPhone(), obj.getUsername());
    }
}
