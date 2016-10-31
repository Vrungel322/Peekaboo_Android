package com.peekaboo.presentation.services;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
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
        void onMessageSent();
        void onMessageNotSent();
    }

    public static void addListener(SmsReceiverListener listener) {
        listeners.add(listener);
    }

    public static void removeListener(SmsReceiverListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals("android.provider.Telephony.SMS_SENT")){
            for (SmsReceiverListener listener : listeners) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        listener.onMessageSent();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        listener.onMessageNotSent();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        listener.onMessageNotSent();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        listener.onMessageNotSent();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        listener.onMessageNotSent();
                        break;
                }
            }
        } else if (action.equals("android.provider.Telephony.SMS_RECEIVED")){
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
}
