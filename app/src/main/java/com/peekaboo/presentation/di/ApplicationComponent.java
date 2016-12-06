package com.peekaboo.presentation.di;

import com.peekaboo.data.di.UserComponent;
import com.peekaboo.data.di.UserModule;
import com.peekaboo.data.rest.PeekabooApi;
import com.peekaboo.utils.ActivityNavigator;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    UserComponent plus(UserModule userModule);

    PeekabooApi api();
    ActivityNavigator navigator();
}
