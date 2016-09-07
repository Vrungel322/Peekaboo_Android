package com.peekaboo.presentation.presenters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;

import com.peekaboo.data.mappers.AbstractMapperFactory;
import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.data.repositories.database.messages.PMessageAbs;
import com.peekaboo.data.repositories.database.messages.PMessageHelper;
import com.peekaboo.domain.AudioRecorder;
import com.peekaboo.domain.Record;
import com.peekaboo.presentation.adapters.ChatAdapter;
import com.peekaboo.presentation.views.IChatView;
import com.peekaboo.utils.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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
    private MediaPlayer mPlayer;

    private String receiver;
    private Timer mTimer;


    @Inject
    public ChatPresenter(PMessageHelper pMessageHelper,
                         AbstractMapperFactory mapperFactory, TextToSpeech textToSpeech) {
        this.pMessageHelper = pMessageHelper;
        this.mapperFactory = mapperFactory;
        this.textToSpeech = textToSpeech;
    }

    @Override
    public void onChatHistoryLoading(Action1 adapter) {
        pMessageHelper.createTable(receiver);
        subscriptions.add(pMessageHelper.getAllMessages(receiver).subscribe(adapter));

        subscriptions.add(pMessageHelper.getUnreadMessages(receiver, false).subscribe(pMessageAbses -> {
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
                //TODO send audio message

//                pMessageHelper.insert(receiver, new PMessage(
//                        true, PMessageAbs.PMESSAGE_MEDIA_TYPE.AUDIO_MESSAGE, record.getFilename(),
//                        System.currentTimeMillis()));
            });
        }
    }

    @Override
    public void onSendTextButtonPress() {
        if (getView() != null) {
            String msgBody = getView().getMessageText();
            if (!TextUtils.isEmpty(msgBody)) {
                // for test
                Random random = new Random();
                boolean isMine = random.nextBoolean();
                //
//                pMessageHelper.insert(receiver, convertPMessage(new TextPMessage(Utility.getPackageId(),
//                        isMine, msgBody, System.currentTimeMillis(),
//                        false, false, false)));

                //TODO: send text message
            }
            getView().clearTextField();
        }
    }

    @Override
    public void onSendImageButtonPress(Uri uri) {
        //TODO send image message
//        pMessageHelper.insert(receiver, convertPMessage(new PMessage(Utility.getPackageId(),
//                true, uri.toString(), System.currentTimeMillis(),
//                false, false, false)));
    }

    @Override
    public void onSendAudioButtonPress(Intent data) {
        ArrayList<String> result = data
                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        //TODO send audio file
//        pMessageHelper.insert(receiver, convertPMessage(new AudioPMessage(Utility.getPackageId(),
//                true, result.get(0), System.currentTimeMillis(),
//                false, false, false)));
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
        pMessageHelper.deleteMessageByPackageId(receiver, message);
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
    public void onStartPlayingAudioClick(String filepath, int position) {
        if (!TextUtils.isEmpty(filepath)) {
            mPlayer = new MediaPlayer();
            mPlayer.reset();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setScreenOnWhilePlaying(true);
            try {
                mPlayer.setDataSource(filepath);
                mPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mTimer = new Timer();

            mPlayer.setOnPreparedListener(mp1 -> {
                mPlayer.start();

                IChatView view = getView();
                if (view != null) {
                    view.switchPlayButtonImage(position, false);
                }

                long totalDuration = mPlayer.getDuration();
                int updateTime = (int) totalDuration/100;

                mTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        long currentDuration = mPlayer.getCurrentPosition();
                        int progress = Utility.getProgressPercentage(currentDuration, totalDuration);

                        IChatView view = getView();
                        if (view != null) {
                            view.updateAudioProgress(position, totalDuration, currentDuration, progress);
                        }
                    }
                }, updateTime, updateTime);
            });
            mPlayer.setOnCompletionListener(mp -> {
                onStopPlayingAudioClick(position);
            });
        }

    }

    @Override
    public void onStopPlayingAudioClick(int position) {
        if(mTimer != null){
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;

        IChatView view = getView();
        if (view != null) {
            view.updateAudioProgress(position, 0, 0, 0);
            view.switchPlayButtonImage(position, true);
        }
    }

    @Override
    public void onStopAndPlayAudioClick(String filepath, int position) {
        if (mPlayer == null) {
            onStartPlayingAudioClick(filepath, position);
        }

        if (mPlayer.isPlaying()) {
            onStopPlayingAudioClick(position);
        } else {
            onStartPlayingAudioClick(filepath, position);
        }
    }

    @Override
    public void onDetachedFromRecyclerView() {
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }
}