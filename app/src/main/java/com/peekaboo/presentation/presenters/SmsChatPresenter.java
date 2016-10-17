package com.peekaboo.presentation.presenters;

import android.telephony.SmsMessage;
import android.util.Log;

import com.peekaboo.domain.Sms;
import com.peekaboo.domain.UserMessageMapper;
import com.peekaboo.domain.subscribers.BaseProgressSubscriber;
import com.peekaboo.domain.usecase.GetAllSmsUseCase;
import com.peekaboo.domain.usecase.GetContactSmsUseCase;
import com.peekaboo.presentation.services.ISMSManager;
import com.peekaboo.presentation.services.SmsReceiver;
import com.peekaboo.presentation.views.ISmsChatView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by st1ch on 10.10.2016.
 */

public class SmsChatPresenter extends ProgressPresenter<ISmsChatView> implements ISmsChatPresenter,
        SmsReceiver.SmsReceiverListener {

    private final ISMSManager smsManager;
    private String receiverPhone;
    private GetAllSmsUseCase getAllSmsUseCase;
    private GetContactSmsUseCase getContactSmsUseCase;

    @Inject
    public SmsChatPresenter(UserMessageMapper errorHandler, ISMSManager smsManager,
                            GetAllSmsUseCase getAllSmsUseCase, GetContactSmsUseCase getContactSmsUseCase) {
        super(errorHandler);
        this.smsManager = smsManager;
        this.getAllSmsUseCase = getAllSmsUseCase;
        this.getContactSmsUseCase = getContactSmsUseCase;
    }

    @Override
    public void onResume(String receiverPhone) {
        SmsReceiver.addListener(this);
        this.receiverPhone = receiverPhone;
        getAllContactsMessageList();
    }

    @Override
    public void onDestroy() {
        SmsReceiver.removeListener(this);
        getAllSmsUseCase.unsubscribe();
        getContactSmsUseCase.unsubscribe();
        unbind();
    }

    @Override
    public void sendMessage(String message) {
        smsManager.sendMessage(message, receiverPhone);
    }

    @Override
    public void getAllContactsMessageList() {
        getContactSmsUseCase.setPhoneNumber(receiverPhone);
        getContactSmsUseCase.execute(getContactsMessagesSubscriber());
    }

    private BaseProgressSubscriber<List<Sms>> getContactsMessagesSubscriber() {
        return new BaseProgressSubscriber<List<Sms>>(this) {
            @Override
            public void onNext(List<Sms> response) {
                super.onNext(response);

                for(Sms sms: response){
                    String body = sms.getBody();
                    String phone = sms.getAddress();
                    int type = sms.getType();

                    ISmsChatView view = getView();
                    if(view != null){
                        view.showMessage(body, phone);
                    }
                    Log.wtf("SmsChatPresenter Contact", "Phone: " + phone +
                            " ;Body: " + body +
                            " ;Type: " + type);

                }
            }
        };
    }

    @Override
    public void onMessageReceived(SmsMessage message) {
//        String text = message.getMessageBody();
//        String sender = message.getOriginatingAddress();
//
//        ISmsChatView view = getView();
//        if(view != null){
//            view.showMessage(text, sender);
//            view.showToastMessage("phone: " + sender + " : " + text);
//        }

    }

}
