package com.peekaboo.presentation.presenters;

import android.util.Log;

import com.peekaboo.data.di.scope.UserScope;
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

@UserScope
public class DialogPresenter extends ProgressPresenter<IDialogsView>
        implements IDialogPresenter, IMessenger.MessengerListener {

    private final IMessenger messenger;
    private GetDialogsListUseCase getDialogsListUseCase;

    @Inject
    public DialogPresenter(UserMessageMapper errorHandler,
                           IMessenger messenger,
                           GetDialogsListUseCase getDialogsListUseCase) {
        super(errorHandler);
        this.messenger = messenger;
        this.getDialogsListUseCase = getDialogsListUseCase;
    }

    @Override
    public void onPause() {
        messenger.removeMessageListener(this);
    }

    @Override
    public void onResume() {
        messenger.addMessageListener(this);
        loadDialogList();

    }

    @Override
    public void unbind() {
        getDialogsListUseCase.unsubscribe();
        super.unbind();
    }

    public void loadDialogList() {
        getDialogsListUseCase.execute(getDialogsListSubscriber());
    }

    private BaseProgressSubscriber<List<Dialog>> getDialogsListSubscriber() {
        return new BaseProgressSubscriber<List<Dialog>>(this) {
            @Override
            public void onNext(List<Dialog> response) {
                super.onNext(response);
                IDialogsView view = getView();
                if (view != null) {
                    Collections.sort(response, new DialogComparator());
                    view.showDialogsList(response);
                }
            }
        };
    }

    @Override
    public void onMessageUpdated(PMessage message) {
        getDialogsListUseCase.execute(getDialogsListSubscriber());
    }

    @Override
    public int displayStatus(PMessage message) {
        return message.status();
    }
}
