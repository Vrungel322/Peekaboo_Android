package com.peekaboo.data.repositories.database.messages;

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
                PMessageAbs.MEDIA_TYPE       + " INTEGER DEFAULT 0 NOT NULL," +
                PMessageAbs.MESSAGE_BODY     + " TEXT NOT NULL," +
                PMessageAbs.TIMESTAMP        + " INTEGER NOT NULL," +
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
        return db.createQuery(tableName, selectAll)
                .mapToList(PMessageAbs.MAPPER)
                .observeOn(AndroidSchedulers.mainThread());
    }

    public int updateMessageByPackageId(String tableName, ContentValues cv, String packageId){
        String WHERE = PMessageAbs.PACKAGE_ID + " = ?";
        return db.update(tableName, cv, WHERE, packageId);
    }

    public int deleteMessageByPackageId(String tableName, String packageId){
        String WHERE = PMessageAbs.PACKAGE_ID + " = ?";
        return db.delete(tableName, WHERE, packageId);
    }

    public void dropTableAndCreate(String tableName){
        String dropTable = "DROP TABLE IF EXISTS " + " " + tableName;
        db.execute(dropTable);
        createTable(tableName);
    }

    public Observable<List<PMessageAbs>> getUnreadMessagesCount(String tableName) {
        String selectUnread = "SELECT * FROM " + tableName + " WHERE " + PMessageAbs.STATUS_READ + " = 0";
        return db.createQuery(tableName, selectUnread)
                .mapToList(PMessageAbs.MAPPER)
                .observeOn(AndroidSchedulers.mainThread());
    }
}
