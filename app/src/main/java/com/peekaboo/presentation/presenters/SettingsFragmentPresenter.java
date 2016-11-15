package com.peekaboo.presentation.presenters;

import android.content.Context;
import android.net.Uri;

import com.peekaboo.data.FileEntity;
import com.peekaboo.domain.UserMessageMapper;
import com.peekaboo.domain.subscribers.BaseProgressSubscriber;
import com.peekaboo.domain.usecase.AvatarUpdateUseCase;
import com.peekaboo.presentation.fragments.ISettingsView;
import com.peekaboo.presentation.utils.ResourcesUtils;

import javax.inject.Inject;

/**
 * Created by Nikita on 02.11.2016.
 */

public class SettingsFragmentPresenter extends ProgressPresenter<ISettingsView> implements ISettingsPresenter<ISettingsView> {

    private Context mContext;
    private AvatarUpdateUseCase avatarUpdateUseCase;

    @Inject
    public SettingsFragmentPresenter(Context context,UserMessageMapper errorHandler,
                                     AvatarUpdateUseCase avatarUpdateUseCase) {
        super(errorHandler);
        this.mContext = context;
        this.avatarUpdateUseCase = avatarUpdateUseCase;

    }

    @Override
    public void bind(ISettingsView view) {
        super.bind(view);
    }

    @Override
    public void unbind() {
        avatarUpdateUseCase.unsubscribe();
        super.unbind();
    }

    @Override
    public void updateAvatarInSettings(Uri avatarUri) {
        if(getView() != null) {
            getView().showProgress();
        }
        avatarUpdateUseCase.setDataForUpdatingAvatar(ResourcesUtils.getRealPathFromURI(mContext, avatarUri));
        avatarUpdateUseCase.execute(getAvatarSettingsSubscriber());
    }

    public BaseProgressSubscriber<FileEntity> getAvatarSettingsSubscriber() {
        return new BaseProgressSubscriber<FileEntity>(this){
            @Override
            public void onNext(FileEntity response) {
                super.onNext(response);
                if(getView() != null){
                    getView().hideProgress();
                    getView().updateAvatarViewInSettings(response.getResult());
                }
            }
        };
    }
}
