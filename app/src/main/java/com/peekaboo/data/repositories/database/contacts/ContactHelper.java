package com.peekaboo.data.repositories.database.contacts;

import android.content.ContentValues;

import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Nikita on 10.08.2016.
 */
public class ContactHelper {

    BriteDatabase db;

    @Inject
    public ContactHelper(@Named("ContactsDb") BriteDatabase db) {
        this.db = db;
    }

    public long insert(ContentValues contact){
        return db.insert(ContactDBHelper.TABLE_NAME, contact);
    }

    public Observable<List<ContactAbs>> getAllContacts(){
        String tableName = ContactDBHelper.TABLE_NAME;
        String selectAll = "SELECT * FROM " + tableName;
        return db.createQuery(tableName, selectAll)
                .mapToList(ContactAbs.MAPPER)
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void createTable(String tableName) {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + tableName + " " +
                "(" +
                ContactAbs.ID                + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                ContactAbs.CONTACT_NAME      + " TEXT NOT NULL," +
                ContactAbs.CONTACT_SURNAME   + " TEXT NOT NULL," +
                ContactAbs.CONTACT_NICKNAME  + " TEXT NOT NULL," +
                ContactAbs.CONTACT_IS_ONLINE + " INTEGER NOT NULL," +
                ContactAbs.CONTACT_IMG_URI   + " TEXT NOT NULL" +
                ");";
        db.execute(CREATE_TABLE);
    }

    public void dropTableAndCreate(String tableName) {
        String dropTable = "DROP TABLE IF EXISTS " + " " + tableName;
        db.execute(dropTable);
        createTable(tableName);
    }
}
