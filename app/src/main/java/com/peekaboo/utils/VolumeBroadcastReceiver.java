package com.peekaboo.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import com.peekaboo.presentation.services.NotificationService;

public class VolumeBroadcastReceiver extends BroadcastReceiver {
    public VolumeBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        switch (audio.getRingerMode()) {
            case AudioManager.RINGER_MODE_NORMAL:
                NotificationService.launch(context, "com.peekaboo.userMode.normal");
                break;

            case AudioManager.RINGER_MODE_VIBRATE:
            case AudioManager.RINGER_MODE_SILENT:
                NotificationService.launch(context, "com.peekaboo.userMode.silent");
                break;
        }
    }
}

