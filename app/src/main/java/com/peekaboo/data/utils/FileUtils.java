package com.peekaboo.data.utils;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.peekaboo.utils.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

/**
 * Created by sebastian on 12.08.16.
 */
public class FileUtils {

    @Nullable
    public static File writeResponseBodyToDisk(String filePath, ResponseBody body) {
        try {
            File file = new File(filePath);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                }
                outputStream.flush();
                return file;
            } catch (IOException e) {
                return null;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return null;
        }
    }


    @NonNull
    public static String formAudioName(String folderName) {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, Constants.SOUND_RECORDING.AUDIO_RECORDER_FOLDER
                + "/" + folderName);

        if (!file.exists()) {
            file.mkdirs();
        }

        return (file.getAbsolutePath() + "/" + System.currentTimeMillis()
                + Constants.SOUND_RECORDING.AUDIO_RECORDER_FILE_EXT_WAV);
    }
}
