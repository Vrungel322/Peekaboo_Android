package com.peekaboo.presentation.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.peekaboo.data.Constants;
import com.peekaboo.data.di.DataModule;
import com.peekaboo.data.mappers.MapperFactory;
import com.peekaboo.data.repositories.database.utils_db.DbModule;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;
import com.peekaboo.presentation.services.INotifier;
import com.peekaboo.presentation.services.Message;
import com.peekaboo.presentation.services.WebSocketNotifier;
import com.squareup.otto.Bus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
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
        return mContext.getSharedPreferences("com.peekaboo.Peekaboo", mContext.MODE_PRIVATE);
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
    public INotifier<Message> provideNotifier(@Named("domens") List<String> domens) {
//        new
//        return new WebSocketNotifier(Constants.BASE_URL_SOCKET, 5000, new MapperFactory());
        return new WebSocketNotifier(domens.get(1), 5000, new MapperFactory());
    }


//    @Singleton
//    @Provides
//    Errors provideErrors() {
//        return new Errors(application);
//    }
}
