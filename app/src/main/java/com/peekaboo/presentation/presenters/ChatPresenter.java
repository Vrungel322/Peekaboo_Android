package com.peekaboo.presentation.presenters;

import android.content.Context;

import com.peekaboo.presentation.database.DBHelper;
import com.peekaboo.presentation.database.PMessage;

import javax.inject.Inject;

/**
 * Created by Nataliia on 13.07.2016.
 */
public class ChatPresenter {
    private Context context;

    @Inject
    public ChatPresenter(Context context) {
        this.context = context;
    }

    public void initdb(){
        DBHelper.init(context, "message_history");
    }

    public void createTable(String tableName){
        DBHelper.createTable(tableName);
    }

    public void makeNoteInTable(PMessage msg, String tableName){
        DBHelper.insertToTable(msg, tableName);
    }
}
