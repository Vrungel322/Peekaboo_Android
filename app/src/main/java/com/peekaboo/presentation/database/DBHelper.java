package com.peekaboo.presentation.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Nikita on 18.07.2016.
 */
public class DBHelper {

    static SQLiteDatabase mDB;

    public DBHelper() {}

    public static void init(Context context, String dbName){
        mDB = DBInstance.getInstance(context, dbName).getWritableDatabase();
    }

    public static void createTable(String tableName) {
        String createTable = "CREATE TABLE IF NOT EXISTS " + tableName + " " +
                "(" +
                PMessageEntity._ID              + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                PMessageEntity.IDPack           + " TEXT NOT NULL," +
                PMessageEntity.MESSAGE_BODY     + " TEXT NOT NULL," +
                PMessageEntity.TIMESTAMP        + " BIGINT NOT NULL," +
                PMessageEntity.status_SEND      + " INT," +
                PMessageEntity.status_DELIVERED + " INT," +
                PMessageEntity.status_READ      + " INT" +
                ");";
        mDB.execSQL(createTable);
    }

    public static void insertToTable(PMessage msg, String tableName) {
        String insertPersonStmt1 = "INSERT INTO '"+tableName+"'('"
                + PMessageEntity.IDPack             + "', '"
                + PMessageEntity.MESSAGE_BODY       + "', '"
                + PMessageEntity.TIMESTAMP          + "', '"
                + PMessageEntity.status_SEND        + "', '"
                + PMessageEntity.status_DELIVERED   + "', '"
                + PMessageEntity.status_READ        +"') VALUES " +
                "("
                + "'" + msg.getIdPack() + "'"
                + ","
                + "'" + msg.getMesBody() + "'"
                + ","
                + msg.getTimestamp()
                + ","
                + msg.getSend()
                + ","
                + msg.getDelivered()
                + ","
                + msg.getRead()
                + ")";
        mDB.execSQL(insertPersonStmt1);
    }

    public static ArrayList<PMessage> getAllNotes(String tableName) {
        ArrayList<PMessage> list = new ArrayList<PMessage>();
        Cursor c = mDB.rawQuery("SELECT * FROM " + tableName, null);
        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                    list.add(new PMessage(c.getString(c.getColumnIndex(PMessageEntity.MESSAGE_BODY)),
                            c.getInt(c.getColumnIndex(PMessageEntity.status_DELIVERED)),
                            c.getInt(c.getColumnIndex(PMessageEntity.status_READ)),
                            c.getInt(c.getColumnIndex(PMessageEntity.status_SEND)),
                            c.getLong(c.getColumnIndex(PMessageEntity.TIMESTAMP)),
                            c.getString(c.getColumnIndex(PMessageEntity.IDPack))));
                c.moveToNext();
            }
        }
        c.close();
        return list;
    }

    public static void dropTableAndCreate(String table) {
        String dropTable = "DROP TABLE IF EXISTS " + " " + table;
        mDB.execSQL(dropTable);
        createTable(table);
    }

    public static void clseDB(){
        if(mDB != null) mDB.close();
    }


    /**
     Method for testing db
     **/
    public static String getTableAsString(String tableName) {
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
    }
}
