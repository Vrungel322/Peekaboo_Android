package com.peekaboo.presentation.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.StatFs;

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
import com.peekaboo.presentation.services.Message;
import com.peekaboo.presentation.services.Messenger;
import com.peekaboo.presentation.services.WebSocketNotifier;
import com.peekaboo.utils.MainThread;
import com.squareup.otto.Bus;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.UrlConnectionDownloader;

import java.io.File;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
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
        return new AccountUser(prefs,  avatarUrl);
    }

    @Singleton
    @Provides
    public INotifier<Message> provideNotifier(MainThread mainThread, @Named("domens") List<String> domens, AbstractMapperFactory abstractMapperFactory) {
        return new WebSocketNotifier(domens.get(1), 5000, abstractMapperFactory, mainThread);
    }

    @Singleton
    @Provides
    public IMessenger provideMessanger(INotifier<Message> notifier, PMessageHelper helper, ReadMessagesHelper readMessagesHelper, AccountUser user, FileUploadUseCase fileUploadUseCase, FileDownloadUseCase downloadFileUseCase) {
        return new Messenger(notifier, helper, readMessagesHelper, user, fileUploadUseCase, downloadFileUseCase);
    }

    @Provides
    @Named("availableCacheSize")
    public Long provideAvailableCacheSize(@Named("cache") File dir) {
        long size = 0;
        try {
            StatFs statFs = new StatFs(dir.getAbsolutePath());
            int sdkInt = Build.VERSION.SDK_INT;
            long totalBytes;
            long availableBytes;
            if (sdkInt < Build.VERSION_CODES.JELLY_BEAN_MR2) {
                int blockSize = statFs.getBlockSize();
                availableBytes = ((long) statFs.getAvailableBlocks()) * blockSize;
                totalBytes = ((long) statFs.getBlockCount()) * blockSize;
            } else {
                availableBytes = statFs.getAvailableBytes();
                totalBytes = statFs.getTotalBytes();
            }
            // Target at least 90% of available or 25% of total space
            size = (long) Math.min(availableBytes * Constants.CACHE.MAX_AVAILABLE_SPACE_USE_FRACTION,
                    totalBytes * Constants.CACHE.MAX_TOTAL_SPACE_USE_FRACTION);
        } catch (IllegalArgumentException ignored) {
            // ignored
        }
        return size;
    }

    @Provides
    @Named("diskCacheSize")
    public Long provideDiskCacheSize(@Named("availableCacheSize") Long availableCacheSize) {
        long size = Math.min(availableCacheSize, Constants.CACHE.MAX_DISK_CACHE_SIZE);
        return Math.max(size, Constants.CACHE.MIN_DISK_CACHE_SIZE);
    }

    @Singleton
    @Provides
    @Named("cache")
    public File createDefaultCacheDir(Context context) {
        File cacheDir = context.getApplicationContext().getExternalCacheDir();
        if (cacheDir == null)
            cacheDir = context.getApplicationContext().getCacheDir();
        File cache = new File(cacheDir, Constants.CACHE.BIG_CACHE_PATH);
        if (!cache.exists()) {
            cache.mkdirs();
        }
        return cache;
    }

    @Singleton
    @Provides
    public Downloader provideDownloader(Context context, @Named("cache") File cache, @Named("diskCacheSize") Long diskCacheSize){
        try {
            Class.forName("com.squareup.okhttp.OkHttpClient");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        File cacheDir = cache;
            long cacheSize = diskCacheSize;
            OkHttpDownloader downloader = new OkHttpDownloader(cache, diskCacheSize);
            return downloader;
    }

    @Singleton
    @Provides
    public Picasso providePicasso(Context context, @Named("avatar") String url, Downloader downloader){
        return new Picasso.Builder(context)
                .downloader(downloader)
                .build();
    }
}
