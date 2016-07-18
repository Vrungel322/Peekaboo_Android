package com.peekaboo.presentation.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Nikita on 18.07.2016.
 */
public class DBInstance extends SQLiteOpenHelper {

    private static DBInstance DBInstance;

    public static DBInstance getInstance(Context context, String dbName){
        if(DBInstance == null)
            DBInstance = new DBInstance(context, dbName);

        return DBInstance;
    }

    private DBInstance(Context context, String dbName) {
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}

}
