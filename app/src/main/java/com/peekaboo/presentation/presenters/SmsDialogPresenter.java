package com.peekaboo.presentation.presenters;

import android.telephony.SmsMessage;

import com.peekaboo.domain.SmsDialog;
import com.peekaboo.domain.UserMessageMapper;
import com.peekaboo.domain.subscribers.BaseProgressSubscriber;
import com.peekaboo.domain.usecase.GetSmsDialogsListUseCase;
import com.peekaboo.presentation.comparators.SmsDialogComparator;
import com.peekaboo.presentation.services.SmsReceiver;
import com.peekaboo.presentation.views.ISmsDialogsView;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by st1ch on 10.10.2016.
 */

public class SmsDialogPresenter extends ProgressPresenter<ISmsDialogsView>
        implements ISmsDialogPresenter, SmsReceiver.SmsReceiverListener {

    private GetSmsDialogsListUseCase getSmsDialogsListUseCase;

    @Inject
    public SmsDialogPresenter(UserMessageMapper errorHandler, GetSmsDialogsListUseCase getSmsDialogsListUseCase) {
        super(errorHandler);
        this.getSmsDialogsListUseCase = getSmsDialogsListUseCase;
    }

    @Override
    public void onMessageReceived(SmsMessage smsMessage) {
        ISmsDialogsView view = getView();
        if(view != null){
            view.updateList();
        }
    }

    @Override
    public void onMessageSent() {

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onResume() {
        SmsReceiver.addListener(this);
    }

    @Override
    public void onPause() {
        SmsReceiver.removeListener(this);
    }

    @Override
    public void onDestroy() {
        getSmsDialogsListUseCase.unsubscribe();
        unbind();
    }

    @Override
    public void loadDialogsList() {
        getSmsDialogsListUseCase.execute(getSmsDialogSubscriber());
    }

    private BaseProgressSubscriber<List<SmsDialog>> getSmsDialogSubscriber(){
        return new BaseProgressSubscriber<List<SmsDialog>>(this){
            @Override
            public void onNext(List<SmsDialog> response) {
                super.onNext(response);

                ISmsDialogsView view = getView();
                if(view != null){
                    Collections.sort(response, new SmsDialogComparator());
                    view.showDialogsList(response);
                }
            }
        };
    }
}
