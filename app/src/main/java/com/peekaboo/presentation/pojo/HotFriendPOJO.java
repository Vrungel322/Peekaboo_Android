package com.peekaboo.presentation.pojo;

/**
 * Created by Nikita on 31.08.2016.
 */
public class HotFriendPOJO {
    private Integer iconId;
    private Boolean isOnline;

    public HotFriendPOJO(Integer iconId, Boolean isOnline) {
        this.iconId = iconId;
        this.isOnline = isOnline;
    }

    public Integer getIconId() {
        return iconId;
    }

    public void setIconId(Integer iconId) {
        this.iconId = iconId;
    }

    public Boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }
}
