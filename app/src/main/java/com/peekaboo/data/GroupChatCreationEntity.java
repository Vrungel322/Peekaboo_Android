package com.peekaboo.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by arkadii on 11/26/16.
 */

public class GroupChatCreationEntity {
    @SerializedName("chat_users")
    private List<GroupChatMemberEntity> chatUsers;
    private String image;
    @SerializedName("chatname")
    private String chatName;

    public GroupChatCreationEntity(List<GroupChatMemberEntity> chatUsers, String image, String chatName) {
        this.chatUsers = chatUsers;
        this.image = image;
        this.chatName = chatName;
    }

    public List<GroupChatMemberEntity> getChatUsers() {
        return chatUsers;
    }

    public String getImage() {
        return image;
    }

    public String getChatName() {
        return chatName;
    }
}
