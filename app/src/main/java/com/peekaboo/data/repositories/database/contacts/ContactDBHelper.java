package com.peekaboo.data.repositories.database.contacts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nikita on 10.08.2016.
 */
public class ContactDBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "contacts";
    public static final String TABLE_NAME = "contacts_table";
    private static final int DB_VERSION = 1;

    public ContactDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " " +
                "(" +
                ContactAbs.ID                + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                ContactAbs.CONTACT_NAME      + " TEXT NOT NULL," +
                ContactAbs.CONTACT_SURNAME   + " TEXT NOT NULL," +
                ContactAbs.CONTACT_NICKNAME  + " TEXT NOT NULL," +
                ContactAbs.CONTACT_IS_ONLINE + " INTEGER NOT NULL," +
                ContactAbs.CONTACT_IMG_URI   + " TEXT NOT NULL" +
                ");";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTable = "DROP TABLE IF EXISTS " + " " + TABLE_NAME;
        db.execSQL(dropTable);
        onCreate(db);
    }
}
