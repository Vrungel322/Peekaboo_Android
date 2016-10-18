package com.peekaboo.domain;

import com.peekaboo.presentation.pojo.PhoneContactPOJO;

/**
 * Created by st1ch on 17.10.2016.
 */

public class SmsDialog {

    private PhoneContactPOJO contact;
    private Sms lastMessage;
    private int unreadMessagesCount;

    public SmsDialog(PhoneContactPOJO contact, Sms lastMessage, int unreadMessagesCount) {
        this.contact = contact;
        this.lastMessage = lastMessage;
        this.unreadMessagesCount = unreadMessagesCount;
    }

    public PhoneContactPOJO getContact() {
        return contact;
    }

    public void setContact(PhoneContactPOJO contact) {
        this.contact = contact;
    }

    public Sms getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Sms lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getUnreadMessagesCount() {
        return unreadMessagesCount;
    }

    public void setUnreadMessagesCount(int unreadMessagesCount) {
        this.unreadMessagesCount = unreadMessagesCount;
    }
}
