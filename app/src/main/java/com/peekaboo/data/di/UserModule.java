package com.peekaboo.data.di;

import android.content.ContentResolver;
import android.content.SharedPreferences;

import com.peekaboo.data.di.scope.UserScope;
import com.peekaboo.data.mappers.AbstractMapperFactory;
import com.peekaboo.data.mappers.MapperFactory;
import com.peekaboo.data.repositories.SessionDataRepository;
import com.peekaboo.data.repositories.database.contacts.PContactHelper;
import com.peekaboo.data.repositories.database.messages.PMessageHelper;
import com.peekaboo.data.repositories.database.service.ReadMessagesHelper;
import com.peekaboo.data.repositories.database.utils_db.DbModule;
import com.peekaboo.data.rest.RestApi;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.usecase.FileDownloadUseCase;
import com.peekaboo.domain.usecase.FileUploadUseCase;
import com.peekaboo.presentation.services.IMessenger;
import com.peekaboo.presentation.services.INotifier;
import com.peekaboo.presentation.services.Message;
import com.peekaboo.presentation.services.MessageNotificator;
import com.peekaboo.presentation.services.Messenger;
import com.peekaboo.presentation.services.WebSocketNotifier;
import com.peekaboo.utils.MainThread;

import java.util.List;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by arkadii on 11/17/16.
 */

@Module(includes = {DbModule.class})
public class UserModule {

    @Provides
    @UserScope
    public SessionRepository provideRepository(AccountUser user, RestApi restApi,
                                               PContactHelper dbHelper,
                                               PMessageHelper messageHelper,
                                               AbstractMapperFactory mapperFactory,
                                               ContentResolver contentResolver) {
        return new SessionDataRepository(restApi, mapperFactory, user, dbHelper, messageHelper, contentResolver);
    }


    @Provides
    @UserScope
    public AbstractMapperFactory provideMapperFactory(@Named("avatar") String avatarUrl) {
        return new MapperFactory(avatarUrl);
    }


    @Provides
    @UserScope
    public AccountUser provideUser(SharedPreferences prefs, @Named("avatar") String avatarUrl) {
        return new AccountUser(prefs, avatarUrl);
    }

    @Provides
    @UserScope
    public INotifier<Message> provideNotifier(MainThread mainThread, @Named("domens") List<String> domens, AbstractMapperFactory abstractMapperFactory) {
        return new WebSocketNotifier(domens.get(1), 5000, abstractMapperFactory, mainThread);
    }

    @Provides
    @UserScope
    public IMessenger provideMessenger(INotifier<Message> notifier, MessageNotificator messageNotificator, PMessageHelper helper, ReadMessagesHelper readMessagesHelper, AccountUser user, FileUploadUseCase fileUploadUseCase, FileDownloadUseCase downloadFileUseCase) {
        return new Messenger(notifier, helper, messageNotificator, readMessagesHelper, user, fileUploadUseCase, downloadFileUseCase);
    }
}
