package com.peekaboo.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by sebastian on 13.07.16.
 */
public class FilesUtils {
    private static final String IMAGE_DIRECTORY_NAME = "Costo";
    private Context context;

    @Inject
    public FilesUtils(Context context) {
        this.context = context;
    }

    private static File getOutputMediaFile() {
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

    /**
     * Creating file uri to store image/video
     */
    public static Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    public String getRealPathFromURI(Uri contentURI) {
        return getRealPathFromURI(context, contentURI);
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

    public static File saveTempFile(Context c, String fileName, int size) throws IOException {
        Bitmap bitmap = ImageUtils.decodeSampledBitmapFromResource(fileName, size);

        File cacheDir = c.getExternalCacheDir();
        if (!cacheDir.exists()) cacheDir.mkdir();

        File result = new File(cacheDir.getPath() + File.separator
                + System.currentTimeMillis() + ".png");
        FileOutputStream fos = new FileOutputStream(result);
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
        fos.close();
        return result;
    }


    public static void deleteFile(File file) {
        file.delete();
    }
}
