package com.peekaboo.data.repositories.database.service;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.auto.value.AutoValue;
import com.peekaboo.data.repositories.database.utils_db.Db;

import rx.functions.Func1;

/**
 * Created by Nikita on 18.07.2016.
 */
@AutoValue
public abstract class ServiceMessageAbs {

    public static final String ID = "_id";
    public static final String MESSAGE_ID = "MESSAGE_ID";
    public static final String SENDER_ID = "SENDER_ID";
    public static final Func1<Cursor, ServiceMessageAbs> MAPPER = new Func1<Cursor, ServiceMessageAbs>() {
        @Override
        public ServiceMessageAbs call(Cursor cursor) {
            long id = Db.getLong(cursor, ID);
            String messageId = Db.getString(cursor, MESSAGE_ID);
            String senderId = Db.getString(cursor, SENDER_ID);
            return new AutoValue_ServiceMessageAbs(id, messageId, senderId);
        }
    };

    public abstract long id();
    public abstract String messageId();
    public abstract String senderId();

    @Override
    public String toString() {
        return messageId();
    }

    public static final class Builder {
        private final ContentValues cv = new ContentValues();

        public Builder id(long id) {
            cv.put(ID, id);
            return this;
        }

        public Builder messageId(String messageId) {
            cv.put(MESSAGE_ID, messageId);
            return this;
        }


        public Builder senderId(String senderId) {
            cv.put(SENDER_ID, senderId);
            return this;
        }

        public ContentValues build() {
            return cv;
        }
    }
}
