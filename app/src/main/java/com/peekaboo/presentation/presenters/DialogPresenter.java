package com.peekaboo.presentation.presenters;

import com.peekaboo.domain.Dialog;
import com.peekaboo.domain.UserMessageMapper;
import com.peekaboo.domain.subscribers.BaseProgressSubscriber;
import com.peekaboo.domain.usecase.GetDialogsListUseCase;
import com.peekaboo.presentation.views.IDialogsView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by st1ch on 27.09.2016.
 */

public class DialogPresenter extends ProgressPresenter<IDialogsView>
        implements  IDialogPresenter{

    private GetDialogsListUseCase getDialogsListUseCase;

    @Inject
    public DialogPresenter(UserMessageMapper errorHandler, GetDialogsListUseCase getDialogsListUseCase) {
        super(errorHandler);
        this.getDialogsListUseCase = getDialogsListUseCase;
    }

    @Override
    public void onCreate() {

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
                    getView().showDialogsList(response);
                }
            }
        };
    }
}
