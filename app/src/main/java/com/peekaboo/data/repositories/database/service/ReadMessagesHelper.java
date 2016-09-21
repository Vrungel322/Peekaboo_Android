package com.peekaboo.data.repositories.database.service;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.peekaboo.data.repositories.database.utils_db.Db;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by sebastian on 31.08.16.
 */
@Singleton
public class ReadMessagesHelper {
    private static final String TABLE_NAME = "ServiceMessages";
    private DBHelper helper;
    private SubscribeOn subscribeOn;
    private ObserveOn observeOn;

    @Inject
    public ReadMessagesHelper(DBHelper helper, SubscribeOn subscribeOn, ObserveOn observeOn) {
        this.helper = helper;
        this.subscribeOn = subscribeOn;
        this.observeOn = observeOn;
        createTable();
    }

    public void createTable() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " " +
                "(" +
                ServiceMessageAbs.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                ServiceMessageAbs.SENDER_ID + " TEXT NOT NULL" +
                ");";
        db.execSQL(CREATE_TABLE);
    }

    public int delete(String id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String WHERE = ServiceMessageAbs.SENDER_ID + " = ?";
        return db.delete(TABLE_NAME, WHERE, new String[]{String.valueOf(id)});
    }

    public Observable<List<String>> getUnreadMessages() {
        String selectAll = String.format("SELECT DISTINCT %s FROM %s", ServiceMessageAbs.SENDER_ID, TABLE_NAME);
        return select(selectAll)
                .subscribeOn(subscribeOn.getScheduler())
                .observeOn(observeOn.getScheduler());
    }

    @NonNull
    private Observable<List<String>> select(String query) {
        Log.e("helper", query);
        return Observable.create((Observable.OnSubscribe<List<String>>) subscriber -> {
            List<String> messages = new ArrayList<>();
            SQLiteDatabase db = helper.getWritableDatabase();

            Cursor cursor = db.rawQuery(query, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String senderId = Db.getString(cursor, ServiceMessageAbs.SENDER_ID);
                    messages.add(senderId);
                }
                cursor.close();
            }

            subscriber.onNext(messages);
            subscriber.onCompleted();
        });
    }

    public void insert(String senderId) {
        Log.e("helper", "saveContactToDb read " + senderId);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ServiceMessageAbs.SENDER_ID, senderId);

        db.insert(TABLE_NAME, null, contentValues);
    }
}
