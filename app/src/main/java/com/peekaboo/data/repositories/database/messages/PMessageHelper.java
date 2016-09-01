package com.peekaboo.data.repositories.database.messages;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.peekaboo.data.repositories.database.contacts.PContactHelper;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by st1ch on 23.07.2016.
 */
public class PMessageHelper {

    BriteDatabase db;
    private PContactHelper contactHelper;

    @Inject
    public PMessageHelper(BriteDatabase db, PContactHelper contactHelper) {
        this.db = db;
        this.contactHelper = contactHelper;
    }

    public void createTable(String tableName) {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS \"" + tableName + "\" " +
                "(" +
                PMessageAbs.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                PMessageAbs.PACKAGE_ID + " TEXT NOT NULL," +
                PMessageAbs.IS_MINE + " INTEGER NOT NULL," +
                PMessageAbs.MEDIA_TYPE + " INTEGER DEFAULT 0 NOT NULL," +
                PMessageAbs.MESSAGE_BODY + " TEXT NOT NULL," +
                PMessageAbs.TIMESTAMP + " INTEGER NOT NULL," +
                PMessageAbs.STATUS + " INTEGER DEFAULT 0 NOT NULL," +
                PMessageAbs.RECEIVER_ID + " TEXT NOT NULL," +
                PMessageAbs.SENDER_ID + " TEXT NOT NULL" +
                ");";
        db.execute(CREATE_TABLE);
    }

    public long insert(String tableName, ContentValues cv) {
        return db.insert(tableName, cv);
    }

    public Observable<List<PMessage>> getAllMessages(String tableName) {
        String selectAll = "SELECT * FROM \"" + tableName + "\"";
        return db.createQuery(tableName, selectAll)
                .mapToList(PMessageAbs.MAPPER)
                .observeOn(AndroidSchedulers.mainThread())
                .map(map());
    }

    public int updateMessageByPackageId(String tableName, ContentValues cv, String packageId) {
        String WHERE = PMessageAbs.PACKAGE_ID + " = ?";
        return db.update(tableName, cv, WHERE, packageId);
    }

    public int updateStatusByPackageId(String tableName, int status, String packageId) {
        String WHERE = PMessageAbs.PACKAGE_ID + " = ?";
        ContentValues cvStatus = new ContentValues();
        cvStatus.put(PMessageAbs.STATUS, status);
        return db.update(tableName, cvStatus, WHERE, packageId);
    }

    public int deleteMessageByPackageId(String tableName, String packageId) {
        String WHERE = PMessageAbs.PACKAGE_ID + " = ?";
        return db.delete(tableName, WHERE, packageId);
    }

    public void dropTableAndCreate(String tableName) {
        String dropTable = "DROP TABLE IF EXISTS " + " \"" + tableName + "\"";
        db.execute(dropTable);
        createTable(tableName);
    }

    public Observable<List<PMessage>> getUnreadMessages(String tableName, boolean isMine) {
        String selectUnread = "SELECT * FROM \"" + tableName + "\" WHERE "
                + PMessageAbs.STATUS + " = " + PMessageAbs.PMESSAGE_STATUS.STATUS_DELIVERED
                + " AND " + PMessage.IS_MINE + " = " + (isMine ? 1 : 0);

        return db.createQuery(tableName, selectUnread)
                .mapToList(PMessageAbs.MAPPER)
                .observeOn(AndroidSchedulers.mainThread())
                .map(map());
    }

    public Observable<List<PMessage>> getUndeliveredMessages() {
        return contactHelper.getAllContacts()
                .flatMapIterable(l -> l)
                .concatMap(pContactAbs -> {
                    String tableName = pContactAbs.contactId();
                    String selectUnread = String.format("SELECT * FROM \"%s\" WHERE %s = %d AND %s = 1",
                            tableName,
                            PMessageAbs.STATUS,
                            PMessageAbs.PMESSAGE_STATUS.STATUS_SENT,
                            PMessageAbs.IS_MINE);

                    return db.createQuery(tableName, selectUnread).mapToList(PMessageAbs.MAPPER);
                })
                .reduce(new ArrayList<PMessageAbs>(), (result, messages) -> {
                    result.addAll(messages);
                    return result;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .map(map());
    }

    @NonNull
    private Func1<List<PMessageAbs>, List<PMessage>> map() {
        return pMessageAbses -> {
            List<PMessage> messages = new ArrayList<>();
            for (PMessageAbs pMessage : pMessageAbses) {
                messages.add(new PMessage(pMessage.packageId(), pMessage.isMine(),
                        pMessage.mediaType(), pMessage.messageBody(), pMessage.timestamp(),
                        pMessage.status(), pMessage.receiverId(), pMessage.senderId()));
            }
            return messages;
        };
    }
}
