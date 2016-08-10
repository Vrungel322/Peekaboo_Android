package com.peekaboo.data.repositories.database.contacts;

import android.content.ContentValues;

import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Nikita on 10.08.2016.
 */
public class PContactHelper {

    BriteDatabase db;

    @Inject
    public PContactHelper(BriteDatabase db) {
        this.db = db;
    }

    public void createTable(String tableName){
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + tableName + " " +
                "(" +
                PContactAbs.ID                + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                PContactAbs.CONTACT_NAME      + " TEXT NOT NULL," +
                PContactAbs.CONTACT_SURNAME   + " TEXT NOT NULL," +
                PContactAbs.CONTACT_NICKNAME  + " TEXT NOT NULL," +
                PContactAbs.CONTACT_IS_ONLINE + " INTEGER NOT NULL," +
                PContactAbs.CONTACT_IMG_URI   + " TEXT NOT NULL" +
                ");";
        db.execute(CREATE_TABLE);
    }

    public long insert(String tableName, ContentValues cv){
        return db.insert(tableName, cv);
    }

    public Observable<List<PContactAbs>> getAllContacts(String tableName){
        String selectAll = "SELECT * FROM " + tableName;
        return db.createQuery(tableName, selectAll)
                .mapToList(PContactAbs.MAPPER)
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void dropTableAndCreate(String tableName){
        String dropTable = "DROP TABLE IF EXISTS " + " " + tableName;
        db.execute(dropTable);
        createTable(tableName);
    }
}
