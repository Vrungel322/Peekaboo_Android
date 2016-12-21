package com.peekaboo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.peekaboo.R;

/**
 * Created by arkadii on 11/9/16.
 */

public class IntentUtils {
    public static final int CAMERA_REQUEST_CODE = 1020;
    public static final int GALLERY_REQUEST_CODE = 1021;
    public  static final int VIDEO_REQUEST_CODE = 1022;


    public static boolean takeGalleryImage(Fragment fragment) {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (pickPhoto.resolveActivity(fragment.getActivity().getPackageManager()) != null) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            fragment.startActivityForResult(Intent.createChooser(intent, fragment.getString(R.string.selectFile)), GALLERY_REQUEST_CODE);
            return true;
        }
        return false;
    }

    @Nullable
    public static String capturePhoto(Fragment fragment) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String file = null;
        if (intent.resolveActivity(fragment.getActivity().getPackageManager()) != null) {
            Uri uri = FilesUtils.getOutputMediaFileUri();
            file = uri.getPath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            fragment.startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
        return file;
    }
    @Nullable
    public static String captureVideo(Fragment fragment) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
        String file = null;
        if (intent.resolveActivity(fragment.getActivity().getPackageManager()) != null) {
            Uri uri = FilesUtils.getOutputMediaFileUri();
            file = uri.getPath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

            fragment.startActivityForResult(intent, VIDEO_REQUEST_CODE);
        }
        return file;
    }

    public static String onGalleryActivityResult(Context c, int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST_CODE) {
                return FilesUtils.getRealPathFromURI(c, data.getData());
            }
        }
        return null;
    }
}
