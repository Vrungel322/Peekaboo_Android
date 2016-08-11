package com.peekaboo.data.repositories.database.contacts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nikita on 10.08.2016.
 */
public class PContactDBHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "contacts";
    private static int DB_VERSION = 1;

    public PContactDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
