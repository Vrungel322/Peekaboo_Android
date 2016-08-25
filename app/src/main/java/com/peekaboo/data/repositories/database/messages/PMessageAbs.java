package com.peekaboo.data.repositories.database.messages;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.auto.value.AutoValue;
import com.peekaboo.data.repositories.database.utils_db.Db;

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
    public static final String RECEIVER_ID = "RECEIVER_ID";
    public static final String SENDER_ID = "SENDER_ID";

    public static final String STATUS = "STATUS";
    public static String IS_MINE = "IS_MINE";
    public static String MEDIA_TYPE = "MEDIA_TYPE";

    public abstract long id();
    public abstract String packageId();
    public abstract boolean isMine();
    public abstract int mediaType();
    public abstract String messageBody();
    public abstract long timestamp();
    public abstract int status();
//    public abstract boolean isDelivered();
//    public abstract boolean isRead();
    public abstract String receiverId();
    public abstract String senderId();

    public static final Func1<Cursor, PMessageAbs> MAPPER = new Func1<Cursor, PMessageAbs>() {
        @Override
        public PMessageAbs call(Cursor cursor) {
            long id = Db.getLong(cursor, ID);
            String packageId = Db.getString(cursor, PACKAGE_ID);
            boolean isMine = Db.getBoolean(cursor, IS_MINE);
            int mediaType = Db.getInt(cursor, MEDIA_TYPE);
            String messageBody = Db.getString(cursor, MESSAGE_BODY);
            long timestamp = Db.getLong(cursor, TIMESTAMP);
            int status = Db.getInt(cursor, STATUS);
            String receiverId = Db.getString(cursor, RECEIVER_ID);
            String senderId = Db.getString(cursor, SENDER_ID);
//            boolean isDelivered = Db.getBoolean(cursor, STATUS_DELIVERED);
//            boolean isRead = Db.getBoolean(cursor, STATUS_READ);
            return new AutoValue_PMessageAbs(id, packageId, isMine, mediaType, messageBody, timestamp,
                                          status, receiverId, senderId);
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

        public Builder status(int status){
            cv.put(STATUS, status);
            return this;
        }

        public Builder receiverId(String receiverId){
            cv.put(RECEIVER_ID, receiverId);
            return this;
        }

        public Builder senderId(String senderId){
            cv.put(SENDER_ID, senderId);
            return this;
        }

        public ContentValues build(){
            return cv;
        }
    }

    @Override
    public String toString() {
        return packageId();
    }

    public interface PMESSAGE_STATUS {
        int STATUS_SENT = 0;
        int STATUS_DELIVERED = 1;
        int STATUS_READ = 2;
    }

    public interface PMESSAGE_MEDIA_TYPE {
        int TEXT_MESSAGE = 0;
        int AUDIO_MESSAGE = 1;
        int IMAGE_MESSAGE = 2;
        int VIDEO_MESSAGE = 3;
        int DOCUMENT_MESSAGE = 4;
    }
}
