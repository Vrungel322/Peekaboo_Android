package com.peekaboo.presentation.services;

/**
 * Created by st1ch on 06.10.2016.
 */

public interface ISmsManager {

    void sendMessage(String message, String receiverNumber);

}