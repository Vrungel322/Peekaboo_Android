package com.peekaboo.data.repositories.database.contacts;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.peekaboo.data.mappers.ContactToContentValueMapper;
import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.data.repositories.database.service.DBHelper;
import com.peekaboo.data.repositories.database.utils_db.Db;
import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Observable;

/**
 * Created by Nikita on 10.08.2016.
 */
public class PContactHelper {

    private static final String TABLE_NAME = "Contacts";
    private final DBHelper helper;
    private final SubscribeOn subscribeOn;
    private final ObserveOn observeOn;
    private final ContactToContentValueMapper mapper = new ContactToContentValueMapper();

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
                ContactAbs.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                ContactAbs.CONTACT_ID + " TEXT NOT NULL," +
                ContactAbs.CONTACT_NAME + " TEXT," +
                ContactAbs.CONTACT_SURNAME + " TEXT," +
                ContactAbs.CONTACT_NICKNAME + " TEXT NOT NULL," +
                ContactAbs.CONTACT_IS_ONLINE + " INTEGER NOT NULL," +
//                ContactAbs.CONTACT_IMG_URI + " TEXT " +
                ContactAbs.CONTACT_IMG_URI + " TEXT, " +
                " UNIQUE (" + ContactAbs.CONTACT_ID + ") ON CONFLICT REPLACE" +
                ")" +
                ";";
        db.execSQL(CREATE_TABLE);
    }

    public long insert(Contact contact) {
        SQLiteDatabase db = helper.getWritableDatabase();
        long id = db.insert(TABLE_NAME, null, mapper.transform(contact));

        contact.setId(id);
        return id;
    }

    public Observable<List<Contact>> getAllContacts() {
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
    private Observable<List<Contact>> select(String query) {
        Log.e("helper", query);
        return Observable.create(subscriber -> {
            List<Contact> messages = new ArrayList<>();
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

    private Contact fetchPContact(Cursor cursor) {
        long id = Db.getLong(cursor, ContactAbs.CONTACT_ID);
        String contactId = Db.getString(cursor, ContactAbs.CONTACT_ID);
        String contactName = Db.getString(cursor, ContactAbs.CONTACT_NAME);
        String contactSurname = Db.getString(cursor, ContactAbs.CONTACT_SURNAME);
        String contactNickname = Db.getString(cursor, ContactAbs.CONTACT_NICKNAME);
        boolean isOnline = Db.getBoolean(cursor, ContactAbs.CONTACT_IS_ONLINE);
        String contactImgUri = Db.getString(cursor, ContactAbs.CONTACT_IMG_URI);

        return new Contact(id, contactName, contactSurname, contactNickname, isOnline, contactImgUri, contactId);
    }

    public Observable<Contact> getContactByContactId(String contactId) {
        String selectAll = "SELECT * FROM " + TABLE_NAME + " WHERE " + Contact.CONTACT_ID + " = " + contactId;
        return select(selectAll).filter(contacts -> contacts.size() == 1).map(contacts -> contacts.get(0));
    }

    public Observable<List<Contact>> getContactsForMessages(List<PMessage> pMessages) {
        Set<String> contactIds = new HashSet<>();
        for (PMessage pMessage : pMessages) {
            contactIds.add(pMessage.senderId());
        }
        String inPart = "";
        for (String contactId : contactIds) {
            inPart += contactId + ", ";
        }
        if (!inPart.isEmpty()) {
            inPart = inPart.substring(0, inPart.length() - 2);
        }
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + Contact.CONTACT_ID + " in (" + inPart + ")";
        return select(query);
    }
}
