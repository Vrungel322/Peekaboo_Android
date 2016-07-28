package com.peekaboo.presentation.presenters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.speech.tts.TextToSpeech;

import com.peekaboo.data.mappers.AbstractMapperFactory;
import com.peekaboo.data.repositories.database.PMessage;
import com.peekaboo.data.repositories.database.PMessageAbs;
import com.peekaboo.data.repositories.database.PMessageHelper;
import com.peekaboo.presentation.views.IChatView;

import javax.inject.Inject;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Nataliia on 13.07.2016.
 */
public class ChatPresenter implements IChatPresenter {

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

    @Override
    public void insertMessageToTable(String tableName, PMessage message) {
        pMessageHelper.insert(tableName, mapperFactory.getPMessageMapper().transform(message));

    }

    @Override
    public void dropTableAndCreate(String tableName) {
        pMessageHelper.dropTableAndCreate(tableName);
    }

    @Override
    public Subscription getAllMessages(String tableName, Action1 adapter) {
        return pMessageHelper.getAllMessages(tableName).subscribe(adapter);
    }

//    public Subscription getTableAsString(String tableName) {
//        return pMessageHelper.getAllMessages(tableName)
//                .subscribe(pMessageAbses -> {
//                    for (PMessageAbs message : pMessageAbses) {
//                        Log.wtf("DB_LOG", "ID: " + message.id()
//                                + "; PACKAGE_ID: " + message.packageId()
//                                + "; BODY: " + message.messageBody()
//                                + "; TIMESTAMP: " + message.timestamp()
//                                + "; IS_MINE: " + message.isMine()
//                                + "; IS_SENT: " + message.isSent()
//                                + "; IS_DELIVERED: " + message.isDelivered()
//                                + "; IS_READ: " + message.isRead());
//                    }
//                });
//    }

    @Override
    public int deleteMessageByPackageId(String tableName, PMessageAbs message) {
        return pMessageHelper.deleteMessageByPackageId(tableName, message.packageId());
    }

    @Override
    public void copyMessageText(PMessageAbs message) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", message.messageBody());
        clipboard.setPrimaryClip(clip);
    }

    @Override
    public void convertTextToSpeech(PMessageAbs message) {
        textToSpeech.speak(message.messageBody(), TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onPause(){
        if(textToSpeech != null){
            textToSpeech.stop();
        }
    }

    @Override
    public void onResume() {

    }

    @Override
    public void bind(IChatView view) {

    }

    @Override
    public void unbind() {

    }
}