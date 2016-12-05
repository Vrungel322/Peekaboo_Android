package com.peekaboo.presentation.presenters;

import android.telephony.SmsMessage;

import com.peekaboo.domain.SmsDialog;
import com.peekaboo.domain.UserMessageMapper;
import com.peekaboo.domain.subscribers.BaseProgressSubscriber;
import com.peekaboo.domain.usecase.GetSmsDialogsListUseCase;
import com.peekaboo.domain.usecase.GetSmsDialogsUseCase;
import com.peekaboo.presentation.comparators.SmsDialogComparator;
import com.peekaboo.presentation.services.SmsReceiver;
import com.peekaboo.presentation.views.ISmsDialogsView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by st1ch on 10.10.2016.
 */

public class SmsDialogPresenter extends ProgressPresenter<ISmsDialogsView>
        implements ISmsDialogPresenter, SmsReceiver.SmsReceiverListener {

    private GetSmsDialogsListUseCase getSmsDialogsListUseCase;
    private GetSmsDialogsUseCase getSmsDialogsUseCase;

    @Inject
    public SmsDialogPresenter(UserMessageMapper errorHandler, GetSmsDialogsListUseCase getSmsDialogsListUseCase, GetSmsDialogsUseCase getSmsDialogsUseCase) {
        super(errorHandler);
        this.getSmsDialogsListUseCase = getSmsDialogsListUseCase;
        this.getSmsDialogsUseCase = getSmsDialogsUseCase;
    }

    @Override
    public void onMessageReceived(SmsMessage smsMessage) {
        loadDialogsList();
    }

    @Override
    public void onMessageSent() {

    }

    @Override
    public void onMessageNotSent() {
        if (getView() != null) {
            getView().showToastMessage("Message not sent!");
        }
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onResume(boolean isFirstLaunch) {
        SmsReceiver.addListener(this);
        if(isFirstLaunch){
            loadDialogs();
        } else {
            loadDialogsList();
        }
    }

    @Override
    public void onPause() {
        SmsReceiver.removeListener(this);
    }

    @Override
    public void onDestroy() {
        getSmsDialogsListUseCase.unsubscribe();
        getSmsDialogsUseCase.unsubscribe();
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

    @Override
    public void loadDialogs(){
        getSmsDialogsUseCase.execute(getSmsDialogsSubscriber());
    }

    private BaseProgressSubscriber<SmsDialog> getSmsDialogsSubscriber(){
        return new BaseProgressSubscriber<SmsDialog>(this){
            List<SmsDialog> dialogs = new ArrayList<>();

            @Override
            public void onNext(SmsDialog response) {
                super.onNext(response);

                dialogs.add(response);

                ISmsDialogsView view = getView();
                if(view != null){
                    Collections.sort(dialogs, new SmsDialogComparator());
                    view.showDialogsList(dialogs);
                }
            }

        };
    }
}
