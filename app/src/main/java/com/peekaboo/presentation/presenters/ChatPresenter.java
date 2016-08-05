package com.peekaboo.presentation.presenters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import com.peekaboo.data.mappers.AbstractMapperFactory;
import com.peekaboo.data.repositories.database.PMessage;
import com.peekaboo.data.repositories.database.PMessageAbs;
import com.peekaboo.data.repositories.database.PMessageHelper;
import com.peekaboo.domain.AudioRecorder;
import com.peekaboo.domain.Record;
import com.peekaboo.presentation.views.IChatView;
import com.peekaboo.utils.Utility;

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

    AudioRecorder recorder;

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

    public Subscription getUnreadMessagesCount(String tableName) {
        return pMessageHelper.getUnreadMessagesCount(tableName)
                .subscribe(pMessageAbses ->
                        Toast.makeText(context, "Unread messages = " + pMessageAbses.size(), Toast.LENGTH_SHORT).show());
    }

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
    public Subscription startRecordingAudio(String folderName) {
        recorder = new AudioRecorder(new Record(folderName));
        return recorder.startRecording().subscribe();
    }

    @Override
    public Subscription stopRecordingAudio(String tableName) {
        if (recorder != null) {
            return recorder.stopRecording().subscribe(record -> {
                insertMessageToTable(tableName, new PMessage(Utility.getPackageId(), true, false, record.getFilename(),
                        System.currentTimeMillis(), false, false, false));
            });
        }
        return null;
    }

    @Override
    public void onPause() {
        if (textToSpeech != null) {
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