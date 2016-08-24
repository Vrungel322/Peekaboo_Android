package com.peekaboo.presentation.presenters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;

import com.peekaboo.data.mappers.AbstractMapperFactory;
import com.peekaboo.data.repositories.database.messages.AudioPMessage;
import com.peekaboo.data.repositories.database.messages.ImagePMessage;
import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.data.repositories.database.messages.PMessageAbs;
import com.peekaboo.data.repositories.database.messages.PMessageHelper;
import com.peekaboo.data.repositories.database.messages.TextPMessage;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.domain.AudioRecorder;
import com.peekaboo.domain.MPlayer;
import com.peekaboo.domain.Record;
import com.peekaboo.domain.usecase.Messanger;
import com.peekaboo.presentation.adapters.ChatAdapter;
import com.peekaboo.presentation.services.INotifier;
import com.peekaboo.presentation.views.IChatView;
import com.peekaboo.utils.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.inject.Inject;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Nataliia on 13.07.2016.
 */
public class ChatPresenter extends BasePresenter<IChatView> implements IChatPresenter {

    private CompositeSubscription subscriptions;
    private PMessageHelper pMessageHelper;
    private AbstractMapperFactory mapperFactory;
    private AudioRecorder recorder;
    private TextToSpeech textToSpeech;
    private MPlayer mPlayer;
    private String receiver;
    private Messanger messanger;
    private AccountUser user;

    @Inject
    public ChatPresenter(PMessageHelper pMessageHelper,
                         AbstractMapperFactory mapperFactory, TextToSpeech textToSpeech, Messanger messanger, AccountUser user) {
        this.pMessageHelper = pMessageHelper;
        this.mapperFactory = mapperFactory;
        this.textToSpeech = textToSpeech;
        this.messanger = messanger;
        this.user = user;
    }

    @Override
    public void onChatHistoryLoading(Action1 adapter) {
        pMessageHelper.createTable(receiver);
        subscriptions.add(pMessageHelper.getAllMessages(receiver).subscribe(adapter));

        subscriptions.add(pMessageHelper.getUnreadMessagesCount(receiver).subscribe(pMessageAbses -> {
            if (getView() != null) {
                getView().showToastMessage("Unread messages = " + pMessageAbses.size());
            }
        }));
    }

    @Override
    public void onDeleteChatHistoryButtonPress(ChatAdapter adapter) {
        pMessageHelper.dropTableAndCreate(receiver);
        adapter.clearList();
    }


    @Override
    public void onStartRecordingAudioClick() {
        recorder = new AudioRecorder(new Record(receiver));
        subscriptions.add(recorder.startRecording().subscribe());
    }

    @Override
    public void onStopRecordingAudioClick() {
        if (recorder != null) {
            recorder.stopRecording().subscribe(record -> {
                pMessageHelper.insert(receiver, convertPMessage(new AudioPMessage(Utility.getPackageId(),
                        true, record.getFilename(),
                        System.currentTimeMillis(),
                        PMessageAbs.PMESSAGE_STATUS.STATUS_SENT, receiver, user.getId())));
            });
        }
    }

    @Override
    public void onSendTextButtonPress() {
        IChatView view = getView();
        if (view != null) {
            String msgBody = view.getMessageText();
            if (!TextUtils.isEmpty(msgBody)) {
                // for test
                Random random = new Random();
                boolean isMine = random.nextBoolean();
                //
                TextPMessage pMessage = new TextPMessage(Utility.getPackageId(),
                        isMine, msgBody, System.currentTimeMillis(),
                        PMessageAbs.PMESSAGE_STATUS.STATUS_SENT, receiver, user.getId());
                pMessageHelper.insert(receiver, convertPMessage(pMessage));

                //TODO: actually sending
            }
            view.clearTextField();
        }
    }

    @Override
    public void onSendImageButtonPress(Uri uri) {
        pMessageHelper.insert(receiver, convertPMessage(new ImagePMessage(Utility.getPackageId(),
                true, uri.toString(), System.currentTimeMillis(),
                PMessageAbs.PMESSAGE_STATUS.STATUS_SENT, receiver, user.getId())));
    }

    @Override
    public void onSendAudioButtonPress(Intent data) {
        ArrayList<String> result = data
                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        pMessageHelper.insert(receiver, convertPMessage(new AudioPMessage(Utility.getPackageId(),
                true, result.get(0), System.currentTimeMillis(),
                PMessageAbs.PMESSAGE_STATUS.STATUS_SENT, receiver, user.getId())));
    }


    @Override
    public void onPause() {
        subscriptions.unsubscribe();
    }

    @Override
    public void onResume() {
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void bind(IChatView view, String receiver) {
        super.bind(view);
        this.receiver = receiver;
    }

    @Override
    public void onDeleteMessageClick(PMessageAbs message) {
        pMessageHelper.deleteMessageByPackageId(receiver, message.packageId());
    }

    @Override
    public void onCopyMessageTextClick(ClipboardManager clipboard, PMessageAbs message) {
        ClipData clip = ClipData.newPlainText("", message.messageBody());
        clipboard.setPrimaryClip(clip);
    }

    @Override
    public void onConvertTextToSpeechClick(PMessageAbs message) {
        textToSpeech.speak(message.messageBody(), TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onStartPlayingAudioClick(String filepath) {
        mPlayer = new MPlayer();
        try {
            mPlayer.play(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStopPlayingAudioClick() {
        mPlayer.stop();
    }

    @Override
    public void onStopAndPlayAudioClick(String filepath) {
        if (mPlayer == null) {
            onStartPlayingAudioClick(filepath);
        }
        try {
            mPlayer.stopAndPlay(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetachedFromRecyclerView() {
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }

    private ContentValues convertPMessage(PMessage pMessage) {
        return mapperFactory.getPMessageMapper().transform(pMessage);
    }
}