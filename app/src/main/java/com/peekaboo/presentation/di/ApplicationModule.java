package com.peekaboo.presentation.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.peekaboo.data.Constants;
import com.peekaboo.data.di.DataModule;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.services.ISmsManager;
import com.peekaboo.presentation.services.SMSManager;
import com.peekaboo.utils.MainThread;
import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Module(includes = {DataModule.class})
public class ApplicationModule {
    private final PeekabooApplication application;

    public ApplicationModule(Context application) {
        this.application = (PeekabooApplication) application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    MainThread provideMainThread() {
        return new MainThread(application.getHandler());
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
    @Singleton
    Bus provideEventBus() {
        return new Bus();
    }

    @Provides
    @Named("avatar")
    public String provideAvatarBaseUrl(@Named("domens") List<String> domens) {
        return domens.get(0) + "avatar/";
    }

    @Singleton
    @Provides
    public Picasso providePicasso(Context context) {
        File httpCacheDirectory = new File(context.getApplicationContext().getCacheDir(), "picasso-cache");
//        Log.wtf("Cache_DIR", httpCacheDirectory.getAbsolutePath());
        Cache cache = new Cache(httpCacheDirectory, Constants.CACHE.MIN_DISK_CACHE_SIZE);

        OkHttpClient clientBuilder = new OkHttpClient.Builder().cache(cache).build();
        return new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(clientBuilder))
                .build();
    }

    @Singleton
    @Provides
    public ISmsManager provideSMSManager(Context context) {
        return new SMSManager(context);
    }
}
