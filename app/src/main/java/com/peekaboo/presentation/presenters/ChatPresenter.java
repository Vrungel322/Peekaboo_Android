package com.peekaboo.presentation.presenters;

import android.content.Context;

import com.peekaboo.presentation.database.PChatMessageDBHelper;
import com.peekaboo.presentation.database.PMessage;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by Nataliia on 13.07.2016.
 */
public class ChatPresenter {
    private Context context;
    @Inject
    PChatMessageDBHelper pChatMessageDBHelper;

    @Inject
    public ChatPresenter(Context context) {
        this.context = context;
    }

    public void createTable(String tableName){
        pChatMessageDBHelper.createTable(tableName);
    }

    public void makeNoteInTable(PMessage msg, String tableName){
        pChatMessageDBHelper.insertToTable(msg, tableName);
    }

    public void dropTableAndCreate(String tableName){
        pChatMessageDBHelper.dropTableAndCreate(tableName);
    }

    public ArrayList<PMessage> getAllNotes (String tableName){
        return pChatMessageDBHelper.getAllNotes(tableName);
    }

    public void getTableAsString(String tableName){
        pChatMessageDBHelper.getTableAsString(tableName);
    }

    public void deleteCortegeFromDB(String tableName, int index, String msgBody) {
        pChatMessageDBHelper.deleteCortegeFromDB(tableName, index, msgBody);
    }
}
