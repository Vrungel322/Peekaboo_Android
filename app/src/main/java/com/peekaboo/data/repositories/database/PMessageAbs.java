package com.peekaboo.data.repositories.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;

import com.google.auto.value.AutoValue;

import rx.functions.Func1;

/**
 * Created by Nikita on 18.07.2016.
 */
@AutoValue
public abstract class PMessageAbs {

    public static final String ID = "_id";
    public static final String PACKAGE_ID = "package_id";
    public static final String MESSAGE_BODY = "MESSAGE_BODY";
    public static final String TIMESTAMP = "TIMESTAMP";
    public static final String STATUS_SENT = "IS_SENT";
    public static final String STATUS_DELIVERED = "IS_DELIVERED";
    public static final String STATUS_READ = "IS_READ";
    public static String IS_MINE = "IS_MINE";
    public static String MEDIA_TYPE = "MEDIA_TYPE";

    public abstract long id();
    public abstract String packageId();
    public abstract boolean isMine();
    public abstract int mediaType();
    public abstract String messageBody();
    public abstract long timestamp();
    public abstract boolean isSent();
    public abstract boolean isDelivered();
    public abstract boolean isRead();

    public static final Func1<Cursor, PMessageAbs> MAPPER = new Func1<Cursor, PMessageAbs>() {
        @Override
        public PMessageAbs call(Cursor cursor) {
            long id = Db.getLong(cursor, ID);
            String packageId = Db.getString(cursor, PACKAGE_ID);
            boolean isMine = Db.getBoolean(cursor, IS_MINE);
            int mediaType = Db.getInt(cursor, MEDIA_TYPE);
            String messageBody = Db.getString(cursor, MESSAGE_BODY);
            long timestamp = Db.getLong(cursor, TIMESTAMP);
            boolean isSent = Db.getBoolean(cursor, STATUS_SENT);
            boolean isDelivered = Db.getBoolean(cursor, STATUS_DELIVERED);
            boolean isRead = Db.getBoolean(cursor, STATUS_READ);
            return new AutoValue_PMessageAbs(id, packageId, isMine, mediaType, messageBody, timestamp,
                                          isSent, isDelivered, isRead);
        }
    };

    public static final class Builder{
        private final ContentValues cv = new ContentValues();

        public Builder id(long id){
            cv.put(ID, id);
            return this;
        }

        public Builder packageId(String packageId){
            cv.put(PACKAGE_ID, packageId);
            return this;
        }

        public Builder isMine(boolean isMine){
            cv.put(IS_MINE, isMine);
            return this;
        }

        public Builder mediaType(int mediaType){
            cv.put(MEDIA_TYPE, mediaType);
            return this;
        }

        public Builder messageBody(String messageBody){
            cv.put(MESSAGE_BODY, messageBody);
            return this;
        }

        public Builder timestamp(long timestamp){
            cv.put(TIMESTAMP, timestamp);
            return this;
        }

        public Builder isSent(boolean isSent){
            cv.put(STATUS_SENT, isSent);
            return this;
        }

        public Builder isDelivered(boolean isDelivered){
            cv.put(STATUS_DELIVERED, isDelivered);
            return this;
        }

        public Builder isRead(boolean isRead){
            cv.put(STATUS_READ, isRead);
            return this;
        }

        public ContentValues build(){
            return cv;
        }
    }
}
