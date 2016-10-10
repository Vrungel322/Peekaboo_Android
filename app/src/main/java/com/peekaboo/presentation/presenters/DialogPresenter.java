package com.peekaboo.presentation.presenters;

import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.domain.Dialog;
import com.peekaboo.domain.UserMessageMapper;
import com.peekaboo.domain.subscribers.BaseProgressSubscriber;
import com.peekaboo.domain.usecase.GetDialogsListUseCase;
import com.peekaboo.presentation.comparators.DialogComparator;
import com.peekaboo.presentation.services.IMessenger;
import com.peekaboo.presentation.views.IDialogsView;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by st1ch on 27.09.2016.
 */

public class DialogPresenter extends ProgressPresenter<IDialogsView>
        implements  IDialogPresenter, IMessenger.MessengerListener {

    private GetDialogsListUseCase getDialogsListUseCase;
    private IMessenger messenger;

    @Inject
    public DialogPresenter(UserMessageMapper errorHandler, GetDialogsListUseCase getDialogsListUseCase, IMessenger messenger) {
        super(errorHandler);
        this.getDialogsListUseCase = getDialogsListUseCase;
        this.messenger = messenger;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onPause() {
        messenger.removeMessageListener(this);
        getDialogsListUseCase.unsubscribe();
    }

    @Override
    public void onResume() {
        messenger.addMessageListener(this);
    }

    @Override
    public void onDestroy() {
        unbind();
    }

    @Override
    public void loadDialogList() {
        getDialogsListUseCase.execute(getDialogsListSubscriber());
    }

    private BaseProgressSubscriber<List<Dialog>> getDialogsListSubscriber(){
        return new BaseProgressSubscriber<List<Dialog>>(this){
            @Override
            public void onNext(List<Dialog> response) {
                super.onNext(response);
                if(getView() != null){
                    Collections.sort(response, new DialogComparator());
                    getView().showDialogsList(response);
                }
            }
        };
    }

    @Override
    public void onMessageUpdated(PMessage message) {

    }

    @Override
    public int displayStatus(PMessage message) {
        return message.status();
    }
}
