package com.peekaboo.data.repositories.database.utils_db;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.peekaboo.data.repositories.database.contacts.ContactDBHelper;
import com.peekaboo.data.repositories.database.messages.PMessageDBHelper;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Named;
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
    @Named("MessagesDbHelper")
    @Singleton
    SQLiteOpenHelper provideMessageOpenHelper(Context application) {
        return new PMessageDBHelper(application);
    }

    @Provides
    @Named("ContactsDbHelper")
    @Singleton
    SQLiteOpenHelper provideContactsOpenHelper(Context application) {
        return new ContactDBHelper(application);
    }

    @Provides
    @Singleton
    SqlBrite provideSqlBrite() {
        return SqlBrite.create(message -> Timber.tag("Database").v(message));
    }

    @Provides
    @Named("MessagesDb")
    @Singleton
    BriteDatabase provideMessagesDatabase(SqlBrite sqlBrite,
                                          @Named("MessagesDbHelper") SQLiteOpenHelper helper) {
        BriteDatabase db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.io());
        db.setLoggingEnabled(true);
        return db;
    }

    @Provides
    @Named("ContactsDb")
    @Singleton
    BriteDatabase provideDatabase(SqlBrite sqlBrite,
                                  @Named("ContactsDbHelper") SQLiteOpenHelper helper) {
        BriteDatabase db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.io());
        db.setLoggingEnabled(true);
        return db;
    }
}
