package com.peekaboo.presentation.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.peekaboo.data.di.DataModule;
import com.peekaboo.data.mappers.MapperFactory;
import com.peekaboo.domain.User;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.data.repositories.database.DbModule;
import com.peekaboo.presentation.services.INotifier;
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
    SharedPreferences provideSharedPreferences(Context mContext){
        return mContext.getSharedPreferences("com.peekaboo.Peekaboo", Context.MODE_PRIVATE);
    }


    @Provides
    Bus provideEventBus(){
        return new Bus();
    }


    @Provides
    @Singleton
    public User provideUser(SharedPreferences prefs) {
        return new User(prefs);
    }

    @Singleton
    @Provides
    public INotifier provideNotifier(User user) {
        return new WebSocketNotifier(user, new MapperFactory());
    }

//    @Singleton
//    @Provides
//    Errors provideErrors() {
//        return new Errors(application);
//    }
}
