package com.peekaboo.presentation.presenters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.peekaboo.data.mappers.AbstractMapperFactory;
import com.peekaboo.data.repositories.database.PMessage;
import com.peekaboo.data.repositories.database.PMessageAbs;
import com.peekaboo.data.repositories.database.PMessageHelper;

import javax.inject.Inject;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Nataliia on 13.07.2016.
 */
public class ChatPresenter {

    private Context context;
    PMessageHelper pMessageHelper;
    AbstractMapperFactory mapperFactory;
    TextToSpeech textToSpeech;

    @Inject
    public ChatPresenter(Context context, PMessageHelper pMessageHelper,
                         AbstractMapperFactory mapperFactory, TextToSpeech textToSpeech) {
        this.context = context;
        this.pMessageHelper = pMessageHelper;
        this.mapperFactory = mapperFactory;
        this.textToSpeech = textToSpeech;
    }

    public void createTable(String tableName) {
        pMessageHelper.createTable(tableName);
    }

    public void makeNoteInTable(String tableName, PMessage msg) {
        pMessageHelper.insert(tableName, mapperFactory.getPMessageMapper().transform(msg));
    }

    public void dropTableAndCreate(String tableName) {
        pMessageHelper.dropTableAndCreate(tableName);
    }

    public Subscription getAllMessages(String tableName, Action1 adapter) {
        return pMessageHelper.getAllMessages(tableName).subscribe(adapter);
    }

    public Subscription getTableAsString(String tableName) {
        return pMessageHelper.getAllMessages(tableName)
                .subscribe(pMessageAbses -> {
                    for (PMessageAbs message : pMessageAbses) {
                        Log.wtf("DB_LOG", "ID: " + message.id()
                                + "; PACKAGE_ID: " + message.packageId()
                                + "; BODY: " + message.messageBody()
                                + "; TIMESTAMP: " + message.timestamp()
                                + "; IS_MINE: " + message.isMine()
                                + "; IS_SENT: " + message.isSent()
                                + "; IS_DELIVERED: " + message.isDelivered()
                                + "; IS_READ: " + message.isRead());
                    }
                });
    }

    public int deleteMessageByPackageId(String tableName, PMessageAbs message) {
        return pMessageHelper.deleteMessageByPackageId(tableName, message.packageId());
    }

    public void copyMessageText(PMessageAbs message) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", message.messageBody());
        clipboard.setPrimaryClip(clip);
    }

    public void convertTextToSpeech(PMessageAbs message) {
        textToSpeech.speak(message.messageBody(), TextToSpeech.QUEUE_FLUSH, null);
    }

    public void onPause(){
        if(textToSpeech != null){
            textToSpeech.stop();
        }
    }
}