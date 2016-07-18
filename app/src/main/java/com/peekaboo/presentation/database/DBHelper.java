package com.peekaboo.presentation.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
                "_id        INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "idPack     TEXT NOT NULL," +
                "mesBody    TEXT NOT NULL," +
                "timestamp  BIGINT NOT NULL," +
                "send       INT," +
                "delivered  INT," +
                "read       INT" +
                ");";
        mDB.execSQL(createTable);
    }

    public static void insertToTable(PMessage msg, String tableName) {
        String insertPersonStmt1 = "INSERT INTO '"+tableName+"'('idPack', 'mesBody', 'timestamp', 'send', 'delivered', 'read') VALUES " +
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
                    list.add(new PMessage(c.getString(c.getColumnIndex("mesBody")),
                            c.getInt(c.getColumnIndex("delivered")),
                            c.getInt(c.getColumnIndex("read")),
                            c.getInt(c.getColumnIndex("send")),
                            c.getLong(c.getColumnIndex("timestamp")),
                            c.getString(c.getColumnIndex("idPack"))));
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
}
