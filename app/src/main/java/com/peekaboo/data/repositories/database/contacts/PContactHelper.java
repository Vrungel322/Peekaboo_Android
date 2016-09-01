package com.peekaboo.data.repositories.database.contacts;

import android.content.ContentValues;

import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Nikita on 10.08.2016.
 */
@Singleton
public class PContactHelper {

    BriteDatabase db;
    private static final String TABLE_NAME = "Contacts";

    @Inject
    public PContactHelper(BriteDatabase db) {
        this.db = db;
        createTable();
    }

    public void createTable(){
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " " +
                "(" +
                PContactAbs.ID                + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                PContactAbs.CONTACT_ID        + " TEXT NOT NULL," +
                PContactAbs.CONTACT_NAME      + " TEXT NOT NULL," +
                PContactAbs.CONTACT_SURNAME   + " TEXT NOT NULL," +
                PContactAbs.CONTACT_NICKNAME  + " TEXT NOT NULL," +
                PContactAbs.CONTACT_IS_ONLINE + " INTEGER NOT NULL," +
                PContactAbs.CONTACT_IMG_URI   + " TEXT NOT NULL" +
                ");";
        db.execute(CREATE_TABLE);
    }

    public long insert(ContentValues cv){
        return db.insert(TABLE_NAME, cv);
    }

    public Observable<List<PContactAbs>> getAllContacts(){
        String selectAll = "SELECT * FROM " + TABLE_NAME;
        return db.createQuery(TABLE_NAME, selectAll)
                .mapToList(PContactAbs.MAPPER)
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void dropTableAndCreate(){
        String dropTable = "DROP TABLE IF EXISTS " + " " + TABLE_NAME;
        db.execute(dropTable);
        createTable();
    }
}
