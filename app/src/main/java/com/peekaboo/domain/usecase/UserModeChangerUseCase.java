package com.peekaboo.domain.usecase;

import android.support.annotation.Nullable;
import android.util.Log;

import com.peekaboo.data.di.scope.UserScope;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.presentation.services.INotifier;
import com.peekaboo.presentation.services.Message;
import com.peekaboo.presentation.services.MessageUtils;

import javax.inject.Inject;

/**
 * Created by Nikita on 18.10.2016.
 */
@UserScope
public class UserModeChangerUseCase {
    INotifier<Message> messageINotifier;
    AccountUser user;
    @Nullable
    IModeChangeListener listener;

    public interface IUserMode {
        byte TEXT_MODE = 1;
        byte AUDIO_MODE = 2;
        byte VIDEO_MODE = 3;
        byte ALL_MODE = 0;
    }

    public interface IModeChangeListener {
        void changeModeInView(byte type);
    }

    @Inject
    public UserModeChangerUseCase(INotifier<Message> messageINotifier, AccountUser user) {
        this.messageINotifier = messageINotifier;
        this.user = user;
    }

    public void setListener(@Nullable IModeChangeListener listener) {
        this.listener = listener;
    }

    public void setMode(byte mode) {
        switch (mode) {
            case IUserMode.TEXT_MODE:
                setTextMode();
                notifyListener(mode);
                Log.wtf("mode", String.valueOf(mode));
                break;
            case IUserMode.AUDIO_MODE:
                setAudioMode();
                notifyListener(mode);
                Log.wtf("mode", String.valueOf(mode));
                break;
            case IUserMode.VIDEO_MODE:
                setVideoMode();
                notifyListener(mode);
                Log.wtf("mode", String.valueOf(mode));
                break;
            case IUserMode.ALL_MODE:
                setAllMode();
                notifyListener(mode);
                Log.wtf("mode", String.valueOf(mode));
                break;
        }
    }

    private void notifyListener(byte mode) {
        if (listener != null) {
            listener.changeModeInView(mode);
        }
    }

    public void setTextMode() {
        Message switchModeMessage = MessageUtils.createSwitchModeMessage(IUserMode.TEXT_MODE, user.getId());
        messageINotifier.sendMessage(switchModeMessage);
        user.saveMode(IUserMode.TEXT_MODE);
    }

    public void setAudioMode() {
        Message switchModeMessage = MessageUtils.createSwitchModeMessage(IUserMode.AUDIO_MODE, user.getId());
        messageINotifier.sendMessage(switchModeMessage);
        user.saveMode(IUserMode.AUDIO_MODE);
    }

    public void setVideoMode() {
        Message switchModeMessage = MessageUtils.createSwitchModeMessage(IUserMode.VIDEO_MODE, user.getId());
        messageINotifier.sendMessage(switchModeMessage);
        user.saveMode(IUserMode.VIDEO_MODE);
    }

    public void setAllMode() {
        Message switchModeMessage = MessageUtils.createSwitchModeMessage(IUserMode.ALL_MODE, user.getId());
        messageINotifier.sendMessage(switchModeMessage);
        user.saveMode(IUserMode.ALL_MODE);
    }
}
