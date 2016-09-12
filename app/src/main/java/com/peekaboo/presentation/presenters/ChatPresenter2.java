package com.peekaboo.presentation.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.data.repositories.database.messages.PMessageAbs;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.domain.AudioRecorder;
import com.peekaboo.domain.Record;
import com.peekaboo.presentation.services.IMessenger;
import com.peekaboo.presentation.utils.AsyncAudioPlayer;
import com.peekaboo.presentation.utils.AudioPlayer;
import com.peekaboo.presentation.views.IChatView2;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by sebastian on 24.08.16.
 */
@Singleton
public class ChatPresenter2 extends BasePresenter<IChatView2> implements IChatPresenter2<IChatView2>,
        IMessenger.MessengerListener {
    private IMessenger messenger;
    private AccountUser accountUser;
    private CompositeSubscription subscriptions;
    private AsyncAudioPlayer player;
    private List<String> domens;
    private AudioRecorder recorder;

    @Inject
    public ChatPresenter2(IMessenger messenger, AccountUser accountUser, AsyncAudioPlayer player, @Named("domens") List<String> domens) {
        this.messenger = messenger;
        this.accountUser = accountUser;
        this.player = player;
        this.domens = domens;
    }

    @Override
    public void bind(IChatView2 view) {
        super.bind(view);
        if (!player.isInitialized()) {
            player.preparePlayer();
        }
    }

    @Override
    public void onResume(boolean isFirstLaunch, String receiver) {
        messenger.tryConnect(accountUser.getBearer());
        messenger.addMessageListener(this);

        subscriptions = new CompositeSubscription();

        if (isFirstLaunch) {
            subscriptions.add(messenger.getAllMessages(receiver)
                    .subscribe(pMessageAbses -> {
                        subscriptions.unsubscribe();
                        IChatView2 view = getView();
                        if (view != null) {
                            view.showMessages(pMessageAbses);
                        }
                    }));
        } else {
            Log.e("BUG", "onResume");
            subscriptions.add(messenger.getUnreadMessages(receiver)
                    .subscribe(pMessageAbses -> {
                        Log.e("BUG", "unsubscribe");
                        subscriptions.unsubscribe();
                        IChatView2 view = getView();
                        if (view != null && !pMessageAbses.isEmpty()) {
                            view.appendMessages(pMessageAbses);
                        }
                    }));
        }

    }

    @Override
    public void onPlayButtonClick(PMessage pMessage, AudioPlayer.AudioPlayerListener listener) {

        if (player.getAudioId() != pMessage.id() || player.state() == AudioPlayer.STATE_RESET) {
            player.reset();
            HashMap<String, String> headers = new HashMap<>();
            headers.put("authorization", accountUser.getBearer());
            String url = domens.get(0) + "download/audio/" + pMessage.messageBody();
            player.prepare(pMessage.id(), url, headers, listener);
        } else if (player.state() == AudioPlayer.STATE_PREPARED) {
            Log.e("presenter", "start " + pMessage.id());
            player.start();
        } else if (player.state() == AudioPlayer.STATE_PLAYING) {
            player.pause();
        }
    }

    boolean isRecording;
    @Override
    public void onRecordButtonClick() {
        IChatView2 view = getView();
        if (view != null) {
            if (isRecording) {
                Log.e("presenter", "record stop");
                isRecording = false;
                recorder.stopRecording().subscribe(record -> {
                    Log.e("presenter", "record stopped");
                    IChatView2 view1 = getView();
                    if (view1 != null) {
                        view1.showRecordStop();
                    }
                });
            } else {
                Log.e("presenter", "record start");
                recorder = new AudioRecorder(new Record(view.getCompanionId()));
                isRecording = true;
                recorder.startRecording().subscribe(record -> {
                    Log.e("presenter", "record started");
                    IChatView2 view1 = getView();
                    if (view1 != null) {
                        view1.showRecordStart();
                    }
                });
            }
        }
    }

    @Override
    public void onRecordSend() {

    }

    @Override
    public void onPause() {
        messenger.removeMessageListener(this);
        subscriptions.unsubscribe();
    }

    @Override
    public void unbind() {
        player.setListener(null);
        super.unbind();
    }

    @Override
    public void onSendTextButtonPress(String text) {
        IChatView2 view = getView();
        if (!text.isEmpty() && view != null) {
            String receiver = view.getCompanionId();
            PMessage pMessage = new PMessage(
                    true, PMessageAbs.PMESSAGE_MEDIA_TYPE.TEXT_MESSAGE, text, System.currentTimeMillis(),
                    PMessageAbs.PMESSAGE_STATUS.STATUS_SENT,
                    receiver, accountUser.getId());
            messenger.sendMessage(pMessage);
            view.clearTextField();
        }
    }

    @Override
    public void onUserMessageRead(PMessage message) {
        messenger.readMessage(message);
    }

    private boolean isFromCurrentChat(@NonNull PMessage message, @NonNull IChatView2 view) {
        return (message.senderId().equals(view.getCompanionId()) && !message.isMine())
                || (message.receiverId().equals(view.getCompanionId()) && message.isMine());
    }

    @Override
    public void onMessageUpdated(PMessage message) {
        IChatView2 view = getView();
        if (view != null && isFromCurrentChat(message, view)) {
            view.updateMessage(message);
        }
    }

    @Override
    public int willChangeStatus(PMessage message) {
        IChatView2 view = getView();
        return view != null && isFromCurrentChat(message, view) ?
                PMessageAbs.PMESSAGE_STATUS.STATUS_READ
                : message.status();
    }

    public void setPlayerListener(AudioPlayer.AudioPlayerListener playerListener) {
        player.setListener(playerListener);
    }
}
