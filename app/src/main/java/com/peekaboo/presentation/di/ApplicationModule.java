package com.peekaboo.presentation.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.peekaboo.data.di.DataModule;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;
import com.peekaboo.presentation.PeekabooApplication;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Module(includes = DataModule.class)
public class ApplicationModule {
    private final PeekabooApplication application;

    public ApplicationModule(PeekabooApplication application) {
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
        return mContext.getSharedPreferences("com.peekaboo.Peekaboo", mContext.MODE_PRIVATE);
    }


    @Provides
    Bus provideEventBus(){
        return new Bus();
    }


//    @Singleton
//    @Provides
//    Errors provideErrors() {
//        return new Errors(application);
//    }
}
