package com.peekaboo.data.repositories.database.utils_db;

import android.content.Context;

import com.peekaboo.data.di.scope.UserScope;
import com.peekaboo.data.mappers.AbstractMapperFactory;
import com.peekaboo.data.mappers.JsonMapper;
import com.peekaboo.data.repositories.database.contacts.PContactHelper;
import com.peekaboo.data.repositories.database.messages.PMessageHelper;
import com.peekaboo.data.repositories.database.service.DBHelper;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;

import dagger.Module;
import dagger.Provides;

/**
 * Created by st1ch on 22.07.2016.
 */
@Module
public class DbModule {

    @Provides
    @UserScope
    public DBHelper provideDBHelper(Context context) {
        return new DBHelper(context);
    }

    @Provides
    @UserScope
    public PContactHelper provideContactHelper(DBHelper helper, SubscribeOn subscribeOn, ObserveOn observeOn,
                                               AbstractMapperFactory mapper, JsonMapper jsonMapper) {
        return new PContactHelper(helper, subscribeOn, observeOn, mapper, jsonMapper);
    }


    @Provides
    @UserScope
    public PMessageHelper provideMessageHelper(DBHelper helper, PContactHelper contactHelper,
                                               SubscribeOn subscribeOn, ObserveOn observeOn,
                                               AbstractMapperFactory factory) {
        return new PMessageHelper(helper, contactHelper, subscribeOn, observeOn, factory);
    }
}
