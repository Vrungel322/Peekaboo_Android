package com.peekaboo.presentation.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.peekaboo.data.Constants;
import com.peekaboo.data.di.DataModule;
import com.peekaboo.data.mappers.MapperFactory;
import com.peekaboo.data.repositories.database.messages.PMessageHelper;
import com.peekaboo.data.repositories.database.service.ServiceMessagesHelper;
import com.peekaboo.data.repositories.database.utils_db.DbModule;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;
import com.peekaboo.presentation.services.IMessenger;
import com.peekaboo.presentation.services.INotifier;
import com.peekaboo.presentation.services.Message;
import com.peekaboo.presentation.services.Messenger;
import com.peekaboo.presentation.services.WebSocketNotifier;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Module(includes = {DataModule.class, DbModule.class})
public class ApplicationModule {
    private final Context application;

    public ApplicationModule(Context application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    SubscribeOn provideSubscribeOn() {
        return Schedulers::newThread;
    }

    @Singleton
    @Provides
    ObserveOn provideObserveOn() {
        return AndroidSchedulers::mainThread;
    }

    @Singleton
    @Provides
    SharedPreferences provideSharedPreferences(Context mContext) {
        return mContext.getSharedPreferences("com.peekaboo.Peekaboo", Context.MODE_PRIVATE);
    }


    @Provides
    Bus provideEventBus() {
        return new Bus();
    }


    @Provides
    @Singleton
    public AccountUser provideUser(SharedPreferences prefs) {
        return new AccountUser(prefs);
    }

    @Singleton
    @Provides
//    public INotifier<Message> provideNotifier(@Named("domens") List<String> domens) {
    public INotifier<Message> provideNotifier() {
//        new
        return new WebSocketNotifier(Constants.BASE_URL_SOCKET, 5000, new MapperFactory());
//        return new WebSocketNotifier(domens.get(1), 5000, new MapperFactory());
    }

    @Singleton
    @Provides
    public IMessenger provideMessanger(INotifier<Message> notifier, PMessageHelper helper, ServiceMessagesHelper serviceMessagesHelper, AccountUser user) {
        return new Messenger(notifier, helper, serviceMessagesHelper, user, new MapperFactory());
    }


//    @Singleton
//    @Provides
//    Errors provideErrors() {
//        return new Errors(application);
//    }
}
