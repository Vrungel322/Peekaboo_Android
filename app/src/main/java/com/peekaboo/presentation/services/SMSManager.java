package com.peekaboo.presentation.services;

import android.telephony.SmsManager;

import java.util.ArrayList;

/**
 * Created by st1ch on 06.10.2016.
 */

public class SMSManager implements ISMSManager{

    private SmsManager smsManager = SmsManager.getDefault();

    @Override
    public void sendMessage(String message, String receiverNumber) {
        ArrayList<String> messageParts = smsManager.divideMessage(message);
        smsManager.sendMultipartTextMessage(receiverNumber, null, messageParts, null, null);
    }

}
