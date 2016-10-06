package com.peekaboo.presentation.presenters;

import android.content.Context;
import android.net.Uri;

import com.peekaboo.data.FileEntity;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.domain.Dialog;
import com.peekaboo.domain.UserMessageMapper;
import com.peekaboo.domain.subscribers.BaseProgressSubscriber;
import com.peekaboo.domain.usecase.AvatarUpdateUseCase;
import com.peekaboo.domain.usecase.GetDialogsListUseCase;
import com.peekaboo.presentation.comparators.DialogComparator;
import com.peekaboo.presentation.utils.ResourcesUtils;
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
    private AvatarUpdateUseCase avatarUpdateUseCase;
    private AccountUser accountUser;

    @Inject
    public MainActivityPresenter(Context context, UserMessageMapper errorHandler,
                                 GetDialogsListUseCase getDialogsListUseCase,
                                 AvatarUpdateUseCase avatarUpdateUseCase,
                                 AccountUser accountUser) {
        super(errorHandler);
        this.mContext = context;
        this.getDialogsListUseCase = getDialogsListUseCase;
        this.avatarUpdateUseCase = avatarUpdateUseCase;
        this.accountUser = accountUser;
    }

    @Override
    public void bind(IMainView view) {
        super.bind(view);
    }

    @Override
    public void fillHotAdapter() {
        getDialogsListUseCase.execute(getDialogsListSubscriber());
    }

    @Override
    public void updateAvatar(Uri avatarUri) {
        avatarUpdateUseCase.setDataForUpdatingAvatar(ResourcesUtils.getRealPathFromURI(mContext, avatarUri));
        avatarUpdateUseCase.execute(getAvatarSubscriber());
    }

    public BaseProgressSubscriber<FileEntity> getAvatarSubscriber() {
        return new BaseProgressSubscriber<FileEntity>(this){
            @Override
            public void onNext(FileEntity response) {
                super.onNext(response);
                if(getView() != null){
                    getView().updateAvatarView(response.getResult());
                }
            }
        };
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
