package com.peekaboo.domain;

/**
 * Created by arkadii on 11/26/16.
 */

public class GroupChatMember {
    private String id;
    private String username;
    private String creatorId;

    public GroupChatMember(String id, String username, String creatorId) {
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
