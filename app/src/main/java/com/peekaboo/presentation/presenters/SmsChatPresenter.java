package com.peekaboo.presentation.presenters;

import android.telephony.SmsMessage;
import android.util.Log;

import com.peekaboo.domain.Sms;
import com.peekaboo.domain.SmsDialog;
import com.peekaboo.domain.UserMessageMapper;
import com.peekaboo.domain.subscribers.BaseProgressSubscriber;
import com.peekaboo.domain.usecase.GetAllSmsUseCase;
import com.peekaboo.domain.usecase.GetContactSmsUseCase;
import com.peekaboo.domain.usecase.GetSmsDialogsListUseCase;
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
    private GetSmsDialogsListUseCase getSmsDialogsListUseCase;

    @Inject
    public SmsChatPresenter(UserMessageMapper errorHandler, ISMSManager smsManager,
                            GetAllSmsUseCase getAllSmsUseCase,
                            GetContactSmsUseCase getContactSmsUseCase,
                            GetSmsDialogsListUseCase getSmsDialogsListUseCase) {
        super(errorHandler);
        this.smsManager = smsManager;
        this.getAllSmsUseCase = getAllSmsUseCase;
        this.getContactSmsUseCase = getContactSmsUseCase;
        this.getSmsDialogsListUseCase = getSmsDialogsListUseCase;
    }

    @Override
    public void onResume(String receiverPhone) {
        SmsReceiver.addListener(this);
        this.receiverPhone = receiverPhone;
        getAllContactsMessageList();
        getDialogsList();
    }

    @Override
    public void onDestroy() {
        SmsReceiver.removeListener(this);
        getAllSmsUseCase.unsubscribe();
        getContactSmsUseCase.unsubscribe();
        getSmsDialogsListUseCase.unsubscribe();
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
//                    Log.wtf("SmsChatPresenter Contact", "Phone: " + phone +
//                            " ;Body: " + body +
//                            " ;Type: " + type);

                }
            }
        };
    }

    private void getDialogsList(){
        getSmsDialogsListUseCase.execute(getSmsDialogSubscriber());
    }

    private BaseProgressSubscriber<List<SmsDialog>> getSmsDialogSubscriber(){
        return new BaseProgressSubscriber<List<SmsDialog>>(this){
            @Override
            public void onNext(List<SmsDialog> response) {
                super.onNext(response);

                for(SmsDialog dialog : response){
                    String contactName = dialog.getContact().getName();
                    String phoneNumber = dialog.getContact().getPhone();
                    String lastMessage = dialog.getLastMessage().getBody();
                    int unread = dialog.getUnreadMessagesCount();

                    Log.wtf("SmsChatPresenter DIALOG: ", "name: " + contactName
                    + " ; number: " + phoneNumber + " ; message: " + lastMessage + " ; unread: " + unread);
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
