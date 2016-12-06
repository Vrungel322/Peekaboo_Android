package com.peekaboo.domain;

import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.data.repositories.database.messages.PMessage;

/**
 * Created by st1ch on 27.09.2016.
 */

public class Dialog {
    private Contact contact;
    private PMessage lastMessage;
    private int unreadMessagesCount;

    public int getUnreadMessagesCount() {
        return unreadMessagesCount;
    }

    public void setUnreadMessagesCount(int unreadMessagesCount) {
        this.unreadMessagesCount = unreadMessagesCount;
    }

    public Dialog(Contact contact, PMessage lastMessage) {
        this.contact = contact;
        this.lastMessage = lastMessage;
    }

    public Contact getContact(){
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public PMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(PMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public String toString() {
        return "Dialog{" +
                "contact=" + contact +
                ", lastMessage=" + lastMessage +
                ", unreadMessagesCount=" + unreadMessagesCount +
                '}';
    }
}
