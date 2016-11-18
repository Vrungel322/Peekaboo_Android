package com.peekaboo.presentation.presenters;

import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;

import com.peekaboo.data.repositories.database.messages.PMessageAbs;
import com.peekaboo.presentation.views.IChatView;

import rx.functions.Action1;

/**
 * Created by st1ch on 28.07.2016.
 */
public interface IChatPresenter extends IPresenter<IChatView> {
    void onChatHistoryLoading(Action1 adapter);

    void onStartRecordingAudioClick();

    void onStopRecordingAudioClick();

    void onSendTextButtonPress();

    void onSendImageButtonPress(Uri uri);

    void onSendAudioButtonPress(Intent data);

    void onNavigationButtonClick();

    void onPause();

    void onResume();

    void bind(IChatView view, String receiver);

    void onDeleteMessageClick(PMessageAbs message);

    void onCopyMessageTextClick(ClipboardManager clipboard, PMessageAbs message);

    void onConvertTextToSpeechClick(PMessageAbs message);

    void onStartPlayingAudioClick(String filepath, int position);

    void onStopPlayingAudioClick(int position);

    void onStopAndPlayAudioClick(String filepath, int position);

    void onDetachedFromRecyclerView();
}
