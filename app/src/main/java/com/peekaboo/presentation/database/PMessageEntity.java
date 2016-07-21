package com.peekaboo.presentation.database;

import android.provider.BaseColumns;

/**
 * Created by Nikita on 21.07.2016.
 */
public class PMessageEntity implements BaseColumns {
    public static final String IDPack = "idPack";
    public static final String MESSAGE_BODY = "MESSAGE_BODY";
    public static final String TIMESTAMP = "TIMESTAMP";
    public static final String status_SEND = "status_SEND";
    public static final String status_DELIVERED = "status_DELIVERED";
    public static final String status_READ = "status_READ";
}
