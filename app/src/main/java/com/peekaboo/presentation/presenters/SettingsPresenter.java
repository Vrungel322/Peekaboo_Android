package com.peekaboo.presentation.presenters;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.peekaboo.data.FileEntity;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.domain.User;
import com.peekaboo.domain.UserMessageMapper;
import com.peekaboo.domain.subscribers.BaseProgressSubscriber;
import com.peekaboo.domain.usecase.AvatarUpdateUseCase;
import com.peekaboo.domain.usecase.UpdateAccountUserDataUseCase;
import com.peekaboo.presentation.fragments.ISettingsView;
import com.peekaboo.presentation.utils.ResourcesUtils;

import javax.inject.Inject;

import okhttp3.ResponseBody;

/**
 * Created by Nikita on 02.11.2016.
 */

public class SettingsPresenter extends ProgressPresenter<ISettingsView> implements ISettingsPresenter<ISettingsView> {

    private Context mContext;
    private AvatarUpdateUseCase avatarUpdateUseCase;
    private UpdateAccountUserDataUseCase updateAccountUserDataUseCase;
    private AccountUser accountUser;

    @Inject
    public SettingsPresenter(Context context, UserMessageMapper errorHandler,
                             AvatarUpdateUseCase avatarUpdateUseCase,
                             UpdateAccountUserDataUseCase updateAccountUserDataUseCase, AccountUser accountUser) {
        super(errorHandler);
        this.mContext = context;
        this.avatarUpdateUseCase = avatarUpdateUseCase;
        this.updateAccountUserDataUseCase = updateAccountUserDataUseCase;
        this.accountUser = accountUser;
    }

    @Override
    public void bind(ISettingsView view) {
        super.bind(view);
        view.renderSettings(accountUser);
    }

    @Override
    public void unbind() {
        avatarUpdateUseCase.unsubscribe();
        updateAccountUserDataUseCase.unsubscribe();
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

    @Override
    public void updateAccountData(User accountUser) {
        updateAccountUserDataUseCase.setCredentials(accountUser);
        updateAccountUserDataUseCase.execute(getUpdateDataSubscriber());
    }

    @Override
    public void onNotificationsEnabled() {
        accountUser.saveNotificationsEnabled(true);
    }

    @Override
    public void onNotificationsDisabled() {
        accountUser.saveNotificationsEnabled(false);
    }

    private BaseProgressSubscriber<FileEntity> getAvatarSettingsSubscriber() {
        return new BaseProgressSubscriber<FileEntity>(this){
            @Override
            public void onNext(FileEntity response) {
                super.onNext(response);
                ISettingsView view = getView();
                if(view != null){
                    view.hideProgress();
                    view.updateAvatarViewInSettings(response.getResult());
                }
            }
        };
    }

    private BaseProgressSubscriber<ResponseBody> getUpdateDataSubscriber() {
        return new BaseProgressSubscriber<ResponseBody>(this){
            @Override
            public void onError(Throwable e) {
                Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_LONG).show();
                super.onError(e);
            }

            @Override
            public void onNext(ResponseBody response) {
                ISettingsView view = getView();
                if (view != null) {
                    view.updateAccountUserFromSettings();
                }
                Toast.makeText(mContext, "Data changed", Toast.LENGTH_LONG).show();
                super.onNext(response);
            }
        };
    }

}
