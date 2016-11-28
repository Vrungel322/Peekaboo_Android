package com.peekaboo.data.mappers;

import com.peekaboo.data.GroupChatMemberEntity;
import com.peekaboo.domain.GroupChatMember;

/**
 * Created by arkadii on 11/26/16.
 */

public class GroupChatMemberMapper implements Mapper<GroupChatMember, GroupChatMemberEntity>{
    @Override
    public GroupChatMemberEntity transform(GroupChatMember obj) throws RuntimeException {
        return new GroupChatMemberEntity(obj.getId(), obj.getUsername(), obj.getCreatorId());
    }
}
