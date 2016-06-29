package com.peekaboo.presentation;

import android.app.Application;
import android.content.Context;

import com.peekaboo.presentation.di.ApplicationComponent;
import com.peekaboo.presentation.di.ApplicationModule;
import com.peekaboo.presentation.di.DaggerApplicationComponent;

/**
 * Created by sebastian on 14.06.16.
 */
public class PeekabooApplication extends Application {

    private ApplicationComponent component;

    public static PeekabooApplication getApp(Context context) {
        return (PeekabooApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        buildAppComponent();
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
