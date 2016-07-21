package com.peekaboo.presentation.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Nikita on 18.07.2016.
 */
@Singleton
public class PChatMessageDBHelper extends SQLiteOpenHelper {

    private static PChatMessageDBHelper PChatMessageDBHelper;
    private static String dbName = "privateMessages";
    private static int dbVersion = 1;
    private SQLiteDatabase mDB;

    @Inject
    private PChatMessageDBHelper(Context context) {
        super(context, dbName, null, dbVersion);
        mDB = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}

    public void createTable(String tableName) {
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

    public void insertToTable(PMessage msg, String tableName) {
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

    public ArrayList<PMessage> getAllNotes(String tableName) {
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

    public void dropTableAndCreate(String table) {
        String dropTable = "DROP TABLE IF EXISTS " + " " + table;
        mDB.execSQL(dropTable);
        createTable(table);
    }

    public void clseDB(){
        if(mDB != null) mDB.close();
    }


    /**
     Method for testing db
     **/
    public String getTableAsString(String tableName) {
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
