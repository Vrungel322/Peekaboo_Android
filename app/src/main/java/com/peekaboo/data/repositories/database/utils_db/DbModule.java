package com.peekaboo.data.repositories.database.utils_db;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.peekaboo.data.repositories.database.service.DBHelper;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by st1ch on 22.07.2016.
 */
@Module
public class DbModule {
    @Provides
    @Singleton
    DBHelper provideOpenHelper(Context application) {
        return new DBHelper(application);
    }

//    @Provides
//    @Singleton
//    SqlBrite provideSqlBrite() {
//        return SqlBrite.create(message -> Timber.tag("Database").v(message));
//    }
//
//    @Provides
//    @Singleton
//    BriteDatabase provideDatabase(SqlBrite sqlBrite, SQLiteOpenHelper helper) {
//        BriteDatabase db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.io());
//        db.setLoggingEnabled(true);
//        return db;
//    }
}
