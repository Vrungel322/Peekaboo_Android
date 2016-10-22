package com.peekaboo.presentation.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by st1ch on 10.10.2016.
 */

public class SmsReceiver extends BroadcastReceiver {

    private static Set<SmsReceiverListener> listeners = new HashSet<>();

    public interface SmsReceiverListener {
        void onMessageReceived(SmsMessage smsMessage);
    }

    public static void addListener(SmsReceiverListener listener) {
        listeners.add(listener);
    }

    public static void removeListener(SmsReceiverListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();

        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get("pdus");

            if (sms != null) {
                for (Object sm : sms) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sm);

                    for (SmsReceiverListener listener : listeners) {
                        listener.onMessageReceived(smsMessage);
                    }

                }
            }
        }
    }
}
