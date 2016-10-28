package com.peekaboo.presentation;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.peekaboo.presentation.activities.LogInActivity;
import com.peekaboo.presentation.di.ApplicationComponent;
import com.peekaboo.presentation.di.ApplicationModule;
import com.peekaboo.presentation.di.DaggerApplicationComponent;
import com.peekaboo.presentation.services.NotificationService;
import com.peekaboo.presentation.services.WearLink;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

//import timber.log.Timber;


/**
 * Created by sebastian on 14.06.16.
 */
public class PeekabooApplication extends Application {

    private ApplicationComponent component;
    private Handler handler;
    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken == null) {
                // VKAccessToken is invalid
                Intent intent = new Intent(PeekabooApplication.this, LogInActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    };

    public static PeekabooApplication getApp(Context context) {
        return (PeekabooApplication) context.getApplicationContext();
    }

    public Handler getHandler() {
        return handler;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        buildAppComponent();
        NotificationService.launch(this, NotificationService.ACTION.TRY_CONNECT);
        WearLink.launch(this);
        vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);
//        Timber.plant(new Timber.DebugTree());
    }

    private void buildAppComponent() {
        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }

}
