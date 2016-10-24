package com.peekaboo.presentation.services;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import java.util.ArrayList;

/**
 * Created by st1ch on 06.10.2016.
 */

public class SMSManager implements ISmsManager {

    private Context context;

    private SmsManager smsManager = SmsManager.getDefault();

    public SMSManager(Context context) {
        this.context = context;
    }

    @Override
    public void sendMessage(String message, String receiverNumber) {
        ArrayList<String> messageParts = smsManager.divideMessage(message);
        int partsCount = messageParts.size();
        ArrayList<PendingIntent> sentPendings = new ArrayList<>(partsCount);
        for (int i = 0; i < partsCount; i++) {
            PendingIntent sentPending = PendingIntent.getBroadcast(context,
                    0, new Intent("android.provider.Telephony.SMS_SENT"), 0);
            sentPendings.add(sentPending);
        }
        smsManager.sendMultipartTextMessage(receiverNumber, null, messageParts, sentPendings, null);
    }

}
