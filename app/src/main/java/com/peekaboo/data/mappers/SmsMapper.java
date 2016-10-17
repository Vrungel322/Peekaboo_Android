package com.peekaboo.data.mappers;

import android.database.Cursor;

import com.peekaboo.domain.Sms;

/**
 * Created by st1ch on 12.10.2016.
 */

public class SmsMapper implements Mapper<Cursor, Sms> {
    @Override
    public Sms transform(Cursor obj) throws RuntimeException {
        return new Sms(
                obj.getLong(obj.getColumnIndex(Sms.COLUMN_ID)),
                obj.getString(obj.getColumnIndex(Sms.COLUMN_ADDRESS)),
                obj.getLong(obj.getColumnIndex(Sms.COLUMN_DATE)),
                obj.getString(obj.getColumnIndex(Sms.COLUMN_BODY)),
                obj.getInt(obj.getColumnIndex(Sms.COLUMN_TYPE)));
    }

}
