package com.peekaboo.presentation;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.peekaboo.data.di.UserComponent;
import com.peekaboo.data.di.UserModule;
import com.peekaboo.presentation.activities.LogInActivity;
import com.peekaboo.presentation.di.ApplicationComponent;
import com.peekaboo.presentation.di.ApplicationModule;
import com.peekaboo.presentation.di.DaggerApplicationComponent;
import com.peekaboo.presentation.services.NotificationService;
import com.peekaboo.presentation.services.WearLink;
import com.peekaboo.utils.ActivityNavigator;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

//import timber.log.Timber;


/**
 * Created by sebastian on 14.06.16.
 */
public class PeekabooApplication extends Application {

    private ApplicationComponent component;
    private UserComponent userComponent;
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
        buildUserComponent();
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

    private void buildUserComponent() {
        userComponent = component.plus(new UserModule());
    }


    public UserComponent getComponent() {
        return userComponent;
    }

    public ApplicationComponent getApplicationComponent() {
        return component;
    }

    public void logout() {
        NotificationService.stop(this);
        WearLink.stop(this);
        Log.e("PeekaboApplication", "logout " + userComponent.hashCode() + " " + userComponent.messenger().hashCode());
        userComponent.messenger().disconnect();
        userComponent.accountUser().logout();
        buildUserComponent();
        Log.e("PeekaboApplication", "logout " + userComponent.hashCode() + " " + userComponent.messenger().hashCode());
        WearLink.launch(this);
        ActivityNavigator navigator = component.navigator();
        navigator.startLogInActivity(this, true);
    }

}
