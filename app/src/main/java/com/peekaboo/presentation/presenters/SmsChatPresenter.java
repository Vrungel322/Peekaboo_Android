package com.peekaboo.presentation.presenters;

import android.telephony.SmsMessage;

import com.peekaboo.domain.UserMessageMapper;
import com.peekaboo.presentation.services.ISMSManager;
import com.peekaboo.presentation.services.SmsReceiver;
import com.peekaboo.presentation.views.ISmsChatView;

import javax.inject.Inject;

/**
 * Created by st1ch on 10.10.2016.
 */

public class SmsChatPresenter extends ProgressPresenter<ISmsChatView> implements ISmsChatPresenter,
        SmsReceiver.SmsReceiverListener {

    private final ISMSManager smsManager;
    private String receiverPhone;

    @Inject
    public SmsChatPresenter(UserMessageMapper errorHandler, ISMSManager smsManager) {
        super(errorHandler);
        this.smsManager = smsManager;
    }

    @Override
    public void onResume(String receiverPhone) {
        SmsReceiver.addListener(this);
        this.receiverPhone = receiverPhone;
    }

    @Override
    public void onDestroy() {
        SmsReceiver.removeListener(this);
        unbind();
    }

    @Override
    public void sendMessage(String message) {
        smsManager.sendMessage(message, receiverPhone);
    }

    @Override
    public void onMessageReceived(SmsMessage message) {
        String text = message.getMessageBody();
        String sender = message.getOriginatingAddress();

        ISmsChatView view = getView();
        if(view != null){
            view.showMessage(text, sender);
            view.showToastMessage("phone: " + sender + " : " + text);
        }

    }

}
