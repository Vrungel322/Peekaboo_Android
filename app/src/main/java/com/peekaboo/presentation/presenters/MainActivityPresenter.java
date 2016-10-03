package com.peekaboo.presentation.presenters;

import android.content.Context;

import com.peekaboo.domain.Dialog;
import com.peekaboo.domain.UserMessageMapper;
import com.peekaboo.domain.subscribers.BaseProgressSubscriber;
import com.peekaboo.domain.usecase.GetDialogsListUseCase;
import com.peekaboo.presentation.comparators.DialogComparator;
import com.peekaboo.presentation.views.IMainView;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Nikita on 28.06.2016.
 */
public class MainActivityPresenter extends ProgressPresenter<IMainView> implements IMainPresenter<IMainView> {

    private Context mContext;

    private GetDialogsListUseCase getDialogsListUseCase;

    @Inject
    public MainActivityPresenter(Context context, UserMessageMapper errorHandler,
                                 GetDialogsListUseCase getDialogsListUseCase) {
        super(errorHandler);
        this.mContext = context;
        this.getDialogsListUseCase = getDialogsListUseCase;
    }

    @Override
    public void bind(IMainView view) {
        super.bind(view);
    }

    @Override
    public void fillHotAdapter() {
        getDialogsListUseCase.execute(getDialogsListSubscriber());
    }

    private BaseProgressSubscriber<List<Dialog>> getDialogsListSubscriber(){
        return new BaseProgressSubscriber<List<Dialog>>(this){
            @Override
            public void onNext(List<Dialog> response) {
                super.onNext(response);
                if(getView() != null){
                    Collections.sort(response, new DialogComparator());
                    getView().hotFriendToShow(response);
                }
            }
        };
    }

    @Override
    public void unbind() {
        getDialogsListUseCase.unsubscribe();
        super.unbind();
    }
}
