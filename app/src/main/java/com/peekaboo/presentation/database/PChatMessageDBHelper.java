package com.peekaboo.presentation.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Nikita on 18.07.2016.
 */
@Singleton
public class PChatMessageDBHelper extends SQLiteOpenHelper {

    private static String dbName = "privateMessages";
    private static int dbVersion = 1;
    private SQLiteDatabase mDB;

    @Inject
    public PChatMessageDBHelper(Context context) {
        super(context, dbName, null, dbVersion);
        mDB = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
