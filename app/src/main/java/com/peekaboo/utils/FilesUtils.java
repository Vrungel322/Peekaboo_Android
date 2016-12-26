package com.peekaboo.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.peekaboo.data.repositories.database.messages.PMessageAbs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sebastian on 13.07.16.
 */
@Singleton
public class FilesUtils {
    private static final String IMAGE_DIRECTORY_NAME = "Peekaboo";
    private Context context;

    @Inject
    public FilesUtils(Context context) {
        this.context = context;
    }

    private static File getOutputImageFile() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    private static File getOutputVideoFile() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "VIDEO_" + timeStamp + ".mp4");

        return mediaFile;
    }

    /**
     * Creating file uri to store image/video
     */
    public static Uri getOutputMediaFileUri(int typeOfMediaFile) {
        if (typeOfMediaFile == PMessageAbs.PMESSAGE_MEDIA_TYPE.IMAGE_MESSAGE){
            return Uri.fromFile(getOutputImageFile());
        }
        if (typeOfMediaFile == PMessageAbs.PMESSAGE_MEDIA_TYPE.VIDEO_MESSAGE){
            return Uri.fromFile(getOutputVideoFile());
        }
        return null;
    }

    public String getRealPathFromURI(Uri contentURI) {
        return getRealPathFromURI(context, contentURI);
    }

    public File createUploadableImageFile(String originalFile, int preferredSize) throws IOException {
        double max = 0.8 * Math.sqrt(Runtime.getRuntime().freeMemory() / 4);
        return FilesUtils.saveTempImageFile(context, originalFile, Math.min((int) max, preferredSize));
    }

    public File createUploadableVideoFile(String originalFile, int preferredSize) throws IOException {
        double max = 0.8 * Math.sqrt(Runtime.getRuntime().freeMemory() / 4);
        return FilesUtils.saveTempVideoFile(context, originalFile, Math.min((int) max, preferredSize));
    }

    public static String getRealPathFromURI(Context context, Uri contentURI) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public static File saveTempImageFile(Context c, String fileName, int size) throws IOException {
        Bitmap bitmap = ImageUtils.decodeSampledBitmapFromResource(fileName, size);

        File cacheDir = c.getExternalCacheDir();
        if (!cacheDir.exists()) cacheDir.mkdir();

        File result = new File(cacheDir.getPath() + File.separator
                + System.currentTimeMillis() + ".jpeg");
        FileOutputStream fos = new FileOutputStream(result);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
        fos.close();
        return result;
    }

    public static File saveTempVideoFile(Context c, String fileName, int size) throws IOException {
        File cacheDir = c.getExternalCacheDir();
        if (!cacheDir.exists()) cacheDir.mkdir();

        File result = new File(cacheDir.getPath() + File.separator
                + System.currentTimeMillis() + ".mp4");
        FileOutputStream fos = new FileOutputStream(result);
        fos.close();
        return result;
    }


    public static boolean deleteFile(File file) {
        return file.delete();
    }
}
