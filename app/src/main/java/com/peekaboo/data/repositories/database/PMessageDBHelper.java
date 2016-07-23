package com.peekaboo.data.repositories.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nikita on 18.07.2016.
 */
public class PMessageDBHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "privateMessages";
    private static int DB_VERSION = 1;

    public PMessageDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}

    /**
     Method for testing db
     **/
    /*public String getTableAsString(String tableName) {
        Log.d("DB_LOG", "getTableAsString called");
        String tableString = String.format("Table %s:\n", tableName);
        Cursor c  = mDB.rawQuery("SELECT * FROM " + tableName, null);
        if (c.moveToFirst() ){
            String[] columnNames = c.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            c.getString(c.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (c.moveToNext());
        }
        Log.e("DB_LOG", tableString);
        return tableString;
    }*/

}
