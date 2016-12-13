package com.peekaboo.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by arkadii on 11/26/16.
 */

public class GroupChatMemberEntity implements Serializable {
    private String id;
    private String username;
    @SerializedName("creator_id")
    private String creatorId;

    public GroupChatMemberEntity(String id, String username, String creatorId) {
        this.id = id;
        this.username = username;
        this.creatorId = creatorId;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getCreatorId() {
        return creatorId;
    }
}
