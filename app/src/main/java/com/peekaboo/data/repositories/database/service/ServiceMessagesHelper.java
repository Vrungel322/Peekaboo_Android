package com.peekaboo.data.repositories.database.service;

import android.content.ContentValues;

import com.peekaboo.data.repositories.database.messages.PMessageAbs;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by sebastian on 31.08.16.
 */
@Singleton
public class ServiceMessagesHelper {
    private BriteDatabase db;
    private static final String TABLE_NAME = "ServiceMessages";

    @Inject
    public ServiceMessagesHelper(BriteDatabase db) {
        this.db = db;
        createTable();
    }

    public void createTable() {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " " +
                "(" +
                ServiceMessageAbs.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                ServiceMessageAbs.MESSAGE_ID + " TEXT NOT NULL," +
                ServiceMessageAbs.SENDER_ID + " TEXT NOT NULL" +
                ");";
        db.execute(CREATE_TABLE);
    }

    public int deleteById(long id) {
        String WHERE = ServiceMessageAbs.ID + " = ?";
        return db.delete(TABLE_NAME, WHERE, String.valueOf(id));
    }

    public Observable<List<ServiceMessageAbs>> getUnreadMessages() {
        String selectAll = "SELECT * FROM " + TABLE_NAME;

        return db.createQuery(TABLE_NAME, selectAll)
                .mapToList(ServiceMessageAbs.MAPPER)
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void insert(String messageId, String senderId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ServiceMessageAbs.MESSAGE_ID, messageId);
        contentValues.put(ServiceMessageAbs.SENDER_ID, senderId);

        db.insert(TABLE_NAME, contentValues);
    }
}
