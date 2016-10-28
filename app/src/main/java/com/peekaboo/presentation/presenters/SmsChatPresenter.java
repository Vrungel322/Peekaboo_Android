package com.peekaboo.presentation.presenters;

import android.telephony.SmsMessage;

import com.peekaboo.domain.Sms;
import com.peekaboo.domain.UserMessageMapper;
import com.peekaboo.domain.subscribers.BaseProgressSubscriber;
import com.peekaboo.domain.usecase.GetContactSmsUseCase;
import com.peekaboo.domain.usecase.GetLastSmsUseCase;
import com.peekaboo.presentation.comparators.SmsComparator;
import com.peekaboo.presentation.services.ISmsManager;
import com.peekaboo.presentation.services.SmsReceiver;
import com.peekaboo.presentation.views.ISmsChatView;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by st1ch on 10.10.2016.
 */

public class SmsChatPresenter extends ProgressPresenter<ISmsChatView> implements ISmsChatPresenter,
        SmsReceiver.SmsReceiverListener {

    private final ISmsManager smsManager;
    private String receiverPhone;
    private GetContactSmsUseCase getContactSmsUseCase;
    private GetLastSmsUseCase getLastSmsUseCase;

    @Inject
    SmsChatPresenter(UserMessageMapper errorHandler, ISmsManager smsManager,
                     GetContactSmsUseCase getContactSmsUseCase,
                     GetLastSmsUseCase getLastSmsUseCase) {
        super(errorHandler);
        this.smsManager = smsManager;
        this.getContactSmsUseCase = getContactSmsUseCase;
        this.getLastSmsUseCase = getLastSmsUseCase;
    }

    @Override
    public void onCreate(String receiverPhone) {
        this.receiverPhone = receiverPhone;
        getContactSmsUseCase.setPhoneNumber(receiverPhone);
        getLastSmsUseCase.setPhoneNumber(receiverPhone);
    }

    @Override
    public void onResume() {
        SmsReceiver.addListener(this);
        getAllContactsMessageList();
    }

    @Override
    public void onPause() {
        SmsReceiver.removeListener(this);
    }

    @Override
    public void onDestroy() {
        getContactSmsUseCase.unsubscribe();
        getLastSmsUseCase.unsubscribe();
        unbind();
    }

    @Override
    public void sendMessage(String message) {
        ISmsChatView view = getView();
        if (!message.isEmpty() && view != null) {
            smsManager.sendMessage(message, receiverPhone);
            view.clearTextField();
        }
    }

    @Override
    public void getAllContactsMessageList() {
        getContactSmsUseCase.execute(getContactsMessagesSubscriber());
    }

    private BaseProgressSubscriber<List<Sms>> getContactsMessagesSubscriber() {
        return new BaseProgressSubscriber<List<Sms>>(this) {
            @Override
            public void onNext(List<Sms> response) {
                super.onNext(response);

                ISmsChatView view = getView();
                if (view != null) {
                    Collections.sort(response, new SmsComparator());
                    view.showMessages(response);
                }

            }
        };
    }

    @Override
    public void onMessageReceived(SmsMessage message) {
        String phone = message.getOriginatingAddress();
        if (phone.equals(receiverPhone)) {
            getLastSmsUseCase.execute(getLastSmsSubscriber());
        }
    }

    @Override
    public void onMessageSent() {
        getLastSmsUseCase.execute(getLastSmsSubscriber());
    }

    @Override
    public void onMessageNotSent() {
        ISmsChatView view = getView();
        if(view != null){
            view.showToastMessage("Message not sent!");
        }
    }

    private BaseProgressSubscriber<Sms> getLastSmsSubscriber() {
        return new BaseProgressSubscriber<Sms>(this) {
            @Override
            public void onNext(Sms response) {
                super.onNext(response);

                ISmsChatView view = getView();
                if (view != null) {
                    view.appendMessage(response);
                }
            }
        };
    }

}
