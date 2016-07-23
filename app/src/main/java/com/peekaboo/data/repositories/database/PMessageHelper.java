package com.peekaboo.data.repositories.database;

import android.content.ContentValues;

import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by st1ch on 23.07.2016.
 */
public class PMessageHelper {

    BriteDatabase db;

    @Inject
    public PMessageHelper(BriteDatabase db) {
        this.db = db;
    }

    public void createTable(String tableName){
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + tableName + " " +
                "(" +
                PMessageAbs.ID               + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                PMessageAbs.PACKAGE_ID       + " TEXT NOT NULL," +
                PMessageAbs.IS_MINE          + " INTEGER NOT NULL," +
                PMessageAbs.MESSAGE_BODY     + " TEXT NOT NULL," +
                PMessageAbs.TIMESTAMP        + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL," +
                PMessageAbs.STATUS_SENT      + " INTEGER DEFAULT 0 NOT NULL," +
                PMessageAbs.STATUS_DELIVERED + " INTEGER DEFAULT 0 NOT NULL," +
                PMessageAbs.STATUS_READ      + " INTEGER DEFAULT 0 NOT NULL" +
                ");";
        db.execute(CREATE_TABLE);
    }

    public long insert(String tableName, ContentValues cv){
        return db.insert(tableName, cv);
    }

    public Observable<List<PMessageAbs>> getAllMessages(String tableName){
        String selectAll = "SELECT * FROM " + tableName;
        String order = " ORDER BY ASC";
        return db.createQuery(tableName, selectAll)
                .mapToList(PMessageAbs.MAPPER)
                .observeOn(AndroidSchedulers.mainThread());
    }

    public int updateByPackageId(String tableName, ContentValues cv, String packageId){
        String WHERE = "WHERE " + PMessageAbs.PACKAGE_ID + " = ?";
        return db.update(tableName, cv, WHERE, packageId);
    }

    public void dropTableAndCreate(String tableName){
        String dropTable = "DROP TABLE IF EXISTS " + " " + tableName;
        db.execute(dropTable);
        createTable(tableName);
    }
}
