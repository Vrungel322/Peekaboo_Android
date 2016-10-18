package com.peekaboo.presentation.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.peekaboo.data.Constants;
import com.peekaboo.data.di.DataModule;
import com.peekaboo.data.mappers.AbstractMapperFactory;
import com.peekaboo.data.repositories.database.messages.PMessageHelper;
import com.peekaboo.data.repositories.database.service.ReadMessagesHelper;
import com.peekaboo.data.repositories.database.utils_db.DbModule;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;
import com.peekaboo.domain.usecase.FileDownloadUseCase;
import com.peekaboo.domain.usecase.FileUploadUseCase;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.services.IMessenger;
import com.peekaboo.presentation.services.INotifier;
import com.peekaboo.presentation.services.ISmsManager;
import com.peekaboo.presentation.services.Message;
import com.peekaboo.presentation.services.MessageNotificator;
import com.peekaboo.presentation.services.Messenger;
import com.peekaboo.presentation.services.SMSManager;
import com.peekaboo.presentation.services.WebSocketNotifier;
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

@Module(includes = {DataModule.class, DbModule.class})
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
    Bus provideEventBus() {
        return new Bus();
    }

    @Provides
    @Named("avatar")
    public String provideAvatarBaseUrl(@Named("domens") List<String> domens) {
        return domens.get(0) + "avatar/";
    }

    @Provides
    @Singleton
    public AccountUser provideUser(SharedPreferences prefs, @Named("avatar") String avatarUrl) {
        return new AccountUser(prefs, avatarUrl);
    }

    @Singleton
    @Provides
    public INotifier<Message> provideNotifier(MainThread mainThread, @Named("domens") List<String> domens, AbstractMapperFactory abstractMapperFactory) {
        return new WebSocketNotifier(domens.get(1), 5000, abstractMapperFactory, mainThread);
    }

    @Singleton
    @Provides
    public IMessenger provideMessanger(INotifier<Message> notifier, MessageNotificator messageNotificator, PMessageHelper helper, ReadMessagesHelper readMessagesHelper, AccountUser user, FileUploadUseCase fileUploadUseCase, FileDownloadUseCase downloadFileUseCase) {
        return new Messenger(notifier, helper, messageNotificator, readMessagesHelper, user, fileUploadUseCase, downloadFileUseCase);
    }

    @Singleton
    @Provides
    public Picasso providePicasso(Context context) {
        File httpCacheDirectory = new File(context.getApplicationContext().getCacheDir(), "picasso-cache");
        Log.wtf("Cache_DIR", httpCacheDirectory.getAbsolutePath());
        Cache cache = new Cache(httpCacheDirectory, Constants.CACHE.MIN_DISK_CACHE_SIZE);

        OkHttpClient clientBuilder = new OkHttpClient.Builder().cache(cache).build();
        return new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(clientBuilder))
                .build();
    }

    @Singleton
    @Provides
    public ISmsManager provideSMSManager(){
        return new SMSManager();
    }
}
