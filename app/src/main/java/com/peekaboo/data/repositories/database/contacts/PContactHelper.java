package com.peekaboo.data.repositories.database.contacts;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.peekaboo.data.repositories.database.service.DBHelper;
import com.peekaboo.data.repositories.database.utils_db.Db;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by Nikita on 10.08.2016.
 */
@Singleton
public class PContactHelper {

    private static final String TABLE_NAME = "Contacts";
    private DBHelper helper;
    private SubscribeOn subscribeOn;
    private ObserveOn observeOn;

    @Inject
    public PContactHelper(DBHelper helper, SubscribeOn subscribeOn, ObserveOn observeOn) {
        this.helper = helper;
        this.subscribeOn = subscribeOn;
        this.observeOn = observeOn;
        createTable();
    }

    public void createTable() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " " +
                "(" +
                PContactAbs.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                PContactAbs.CONTACT_ID + " TEXT NOT NULL," +
                PContactAbs.CONTACT_NAME + " TEXT NOT NULL," +
                PContactAbs.CONTACT_SURNAME + " TEXT NOT NULL," +
                PContactAbs.CONTACT_NICKNAME + " TEXT NOT NULL," +
                PContactAbs.CONTACT_IS_ONLINE + " INTEGER NOT NULL," +
                PContactAbs.CONTACT_IMG_URI + " TEXT NOT NULL" +
                ");";
        db.execSQL(CREATE_TABLE);
    }

    public long insert(ContentValues cv) {
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.insert(TABLE_NAME, null, cv);
    }

    public Observable<List<PContact>> getAllContacts() {
        String selectAll = "SELECT * FROM " + TABLE_NAME;
        return select(selectAll)
                .subscribeOn(subscribeOn.getScheduler())
                .observeOn(observeOn.getScheduler());
    }

    public void dropTableAndCreate() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String dropTable = "DROP TABLE IF EXISTS " + " " + TABLE_NAME;
        db.execSQL(dropTable);
        createTable();
    }

    @NonNull
    private Observable<List<PContact>> select(String query) {
        Log.e("helper", query);
        return Observable.create((Observable.OnSubscribe<List<PContact>>) subscriber -> {
            List<PContact> messages = new ArrayList<>();
            SQLiteDatabase db = helper.getWritableDatabase();

            Cursor cursor = db.rawQuery(query, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    messages.add(fetchPContact(cursor));
                }
                cursor.close();
            }

            subscriber.onNext(messages);
            subscriber.onCompleted();
        });
    }

    private PContact fetchPContact(Cursor cursor) {
        String contactId = Db.getString(cursor, PContactAbs.CONTACT_ID);
        String contactName = Db.getString(cursor, PContactAbs.CONTACT_NAME);
        String contactSurname = Db.getString(cursor, PContactAbs.CONTACT_SURNAME);
        String contactNickname = Db.getString(cursor, PContactAbs.CONTACT_NICKNAME);
        boolean isOnline = Db.getBoolean(cursor, PContactAbs.CONTACT_IS_ONLINE);
        String contactImgUri = Db.getString(cursor, PContactAbs.CONTACT_IMG_URI);

        return new PContact(contactName, contactSurname, contactNickname, isOnline, contactImgUri, contactId);
    }
}
