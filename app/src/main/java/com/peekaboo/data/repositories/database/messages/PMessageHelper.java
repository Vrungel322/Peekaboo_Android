package com.peekaboo.data.repositories.database.messages;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.peekaboo.data.mappers.AbstractMapperFactory;
import com.peekaboo.data.mappers.Mapper;
import com.peekaboo.data.repositories.database.contacts.PContactHelper;
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
 * Created by st1ch on 23.07.2016.
 */
@Singleton
public class PMessageHelper {

    private static final String PREFIX = "c";
    private final Mapper<PMessageAbs, ContentValues> pMessageMapper;
    private DBHelper helper;
    private PContactHelper contactHelper;
    private SubscribeOn subscribeOn;
    private ObserveOn observeOn;

    @Inject
    public PMessageHelper(DBHelper helper, PContactHelper contactHelper, SubscribeOn subscribeOn, ObserveOn observeOn, AbstractMapperFactory factory) {
        this.helper = helper;
        this.contactHelper = contactHelper;
        this.subscribeOn = subscribeOn;
        this.observeOn = observeOn;
        pMessageMapper = factory.getPMessageMapper();
    }

    public void createTable(String tableName) {
        tableName = PREFIX + tableName;
        SQLiteDatabase db = helper.getWritableDatabase();
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + tableName + " " +
                "(" +
                PMessageAbs.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                PMessageAbs.IS_MINE + " INTEGER NOT NULL," +
                PMessageAbs.MEDIA_TYPE + " INTEGER DEFAULT 0 NOT NULL," +
                PMessageAbs.MESSAGE_BODY + " TEXT NOT NULL," +
                PMessageAbs.TIMESTAMP + " INTEGER NOT NULL," +
                PMessageAbs.STATUS + " INTEGER DEFAULT 0 NOT NULL," +
                PMessageAbs.RECEIVER_ID + " TEXT NOT NULL," +
                PMessageAbs.SENDER_ID + " TEXT NOT NULL" +
                ");";
        db.execSQL(CREATE_TABLE);
    }

    public Observable<List<PMessage>> getAllMessages(String id) {
        Log.e("helper", "get all messages");
        String tableName = PREFIX + id;
        String selectAll = "SELECT * FROM " + tableName;
        return select(selectAll)
                .subscribeOn(subscribeOn.getScheduler())
                .observeOn(observeOn.getScheduler());
    }

    public Observable<List<PMessage>> getUnreadMessages(String id, boolean isMine) {
        String tableName = PREFIX + id;
        String selectUnread = "SELECT * FROM " + tableName + " WHERE "
                + PMessageAbs.STATUS + " = " + PMessageAbs.PMESSAGE_STATUS.STATUS_DELIVERED
                + " AND " + PMessage.IS_MINE + " = " + (isMine ? 1 : 0);
        return select(selectUnread)
                .subscribeOn(subscribeOn.getScheduler())
                .observeOn(observeOn.getScheduler());

    }

    public Observable<List<PMessage>> getUndeliveredMessages() {
        Log.e("helper", "get undelivered messages");
        return contactHelper.getAllContacts()
                .flatMapIterable(l -> l)
                .concatMap(pContactAbs -> {
                    String tableName = PREFIX + pContactAbs.contactId();
                    String selectUnread = String.format("SELECT * FROM %s WHERE %s = %d AND %s = 1",
                            tableName,
                            PMessageAbs.STATUS,
                            PMessageAbs.PMESSAGE_STATUS.STATUS_SENT,
                            PMessageAbs.IS_MINE);

                    return select(selectUnread);
                })
                .reduce(getInitialValue(), (result, messages) -> {
                    result.addAll(messages);
                    return result;
                })
                .subscribeOn(subscribeOn.getScheduler())
                .observeOn(observeOn.getScheduler());
    }

    public void insert(String tableName, PMessage message) {
        SQLiteDatabase db = helper.getWritableDatabase();
        tableName = PREFIX + tableName;
        message.setId(db.insert(tableName, null, pMessageMapper.transform(message)));
        Log.e("helper", "insert " + message);
    }

    public int updateStatus(String tableName, int status, PMessage message) {
        Log.e("helper", "update status " + status + " " + message);
        message.setStatus(status);
        SQLiteDatabase db = helper.getWritableDatabase();
        tableName = PREFIX + tableName;
        String WHERE = PMessageAbs.ID + " = ?";
        ContentValues cvStatus = new ContentValues();
        cvStatus.put(PMessageAbs.STATUS, status);
        return db.update(tableName, cvStatus, WHERE, new String[]{String.valueOf(message.id())});
    }

    public int updateBody(String tableName, PMessage message, String newBody) {
        Log.e("helper", "update body " + newBody + " " + message);
        message.setMessageBody(newBody);
        SQLiteDatabase db = helper.getWritableDatabase();
        tableName = PREFIX + tableName;
        String WHERE = PMessageAbs.ID + " = ?";
        ContentValues cvStatus = new ContentValues();
        cvStatus.put(PMessageAbs.MESSAGE_BODY, newBody);
        return db.update(tableName, cvStatus, WHERE, new String[]{String.valueOf(message.id())});
    }

    public int deleteMessageByPackageId(String tableName, PMessageAbs message) {
        SQLiteDatabase db = helper.getWritableDatabase();
        tableName = PREFIX + tableName;
        String WHERE = PMessageAbs.ID + " = ?";
        return db.delete(tableName, WHERE, new String[]{String.valueOf(message.id())});
    }

    public void dropTableAndCreate(String tableName) {
        SQLiteDatabase db = helper.getWritableDatabase();
        tableName = PREFIX + tableName;
        String dropTable = "DROP TABLE IF EXISTS " + tableName;
        db.execSQL(dropTable);
        createTable(tableName);
    }

    @NonNull
    private Observable<List<PMessage>> select(String query) {
        Log.e("helper", query);
        return Observable.create((Observable.OnSubscribe<List<PMessage>>) subscriber -> {
            List<PMessage> messages = new ArrayList<>();
            SQLiteDatabase db = helper.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    messages.add(fetchPMessage(cursor));
                }
                cursor.close();
            }

            subscriber.onNext(messages);
            subscriber.onCompleted();
        });
    }

    private PMessage fetchPMessage(Cursor cursor) {
        long id = Db.getLong(cursor, PMessageAbs.ID);
        boolean isMine = Db.getInt(cursor, PMessageAbs.IS_MINE) == 1;
        int mediaType = Db.getInt(cursor, PMessageAbs.MEDIA_TYPE);
        String messageBody = Db.getString(cursor, PMessageAbs.MESSAGE_BODY);
        long timestamp = Db.getLong(cursor, PMessageAbs.TIMESTAMP);
        int status = Db.getInt(cursor, PMessageAbs.STATUS);
        String receiverId = Db.getString(cursor, PMessageAbs.RECEIVER_ID);
        String senderId = Db.getString(cursor, PMessageAbs.SENDER_ID);

        PMessage pMessage = new PMessage(isMine, mediaType, messageBody, timestamp, status, receiverId, senderId);
        pMessage.setId(id);

        return pMessage;
    }

    @NonNull
    private List<PMessage> getInitialValue() {
        return new ArrayList<>();
    }
}
