package com.peekaboo.domain;

import com.google.gson.annotations.SerializedName;
import com.peekaboo.data.GroupChatMemberEntity;

import java.util.List;

/**
 * Created by arkadii on 11/26/16.
 */

public class GroupChat {
    private List<GroupChatMember> members;
    private String groupAvatarUrl;
    private String groupChatName;

    public GroupChat(List<GroupChatMember> members, String groupAvatarUrl, String groupChatName) {
        this.members = members;
        this.groupAvatarUrl = groupAvatarUrl;
        this.groupChatName = groupChatName;
    }

    public List<GroupChatMember> getMembers() {
        return members;
    }

    public String getGroupAvatarUrl() {
        return groupAvatarUrl;
    }

    public String getGroupChatName() {
        return groupChatName;
    }
}
