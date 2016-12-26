package com.peekaboo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.peekaboo.R;
import com.peekaboo.data.mappers.GsonMapper;
import com.peekaboo.data.repositories.database.messages.PMessageAbs;
import com.peekaboo.presentation.fragments.ChatFragment;

/**
 * Created by arkadii on 11/9/16.
 */

public class IntentUtils {
    public static final int CAMERA_REQUEST_CODE_PHOTO = 1020;
    public static final int GALLERY_REQUEST_CODE_PHOTO = 1021;
    public static final int CAMERA_REQUEST_CODE_VIDEO = 1022;
    public static final int GALLERY_REQUEST_CODE_VIDEO = 1023;
    private static GsonMapper mapper = new GsonMapper();
    public static boolean takeGalleryImage(Fragment fragment) {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (pickPhoto.resolveActivity(fragment.getActivity().getPackageManager()) != null) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            fragment.startActivityForResult(Intent.createChooser(intent, fragment.getString(R.string.selectFile)), GALLERY_REQUEST_CODE_PHOTO);
            return true;
        }
        return false;
    }

    @Nullable
    public static String capturePhoto(Fragment fragment) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String file = null;
        if (intent.resolveActivity(fragment.getActivity().getPackageManager()) != null) {
            Uri uri = FilesUtils.getOutputMediaFileUri(PMessageAbs.PMESSAGE_MEDIA_TYPE.IMAGE_MESSAGE);
            file = uri.getPath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            fragment.startActivityForResult(intent, CAMERA_REQUEST_CODE_PHOTO);
        }
        return file;
    }

    @Nullable
    public static String captureVideo(ChatFragment chatFragment) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
        String file = null;
        if (intent.resolveActivity(chatFragment.getActivity().getPackageManager()) != null) {
            Uri uri = FilesUtils.getOutputMediaFileUri(PMessageAbs.PMESSAGE_MEDIA_TYPE.VIDEO_MESSAGE);
            file = uri.getPath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            chatFragment.startActivityForResult(intent, CAMERA_REQUEST_CODE_VIDEO);
        }
        return file;
    }

    public static String onGalleryActivityResult(Context c, int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST_CODE_PHOTO) {
                return FilesUtils.getRealPathFromURI(c, data.getData());
            }
            if (requestCode == CAMERA_REQUEST_CODE_VIDEO) {
                return FilesUtils.getRealPathFromURI(c, data.getData());
            }
        }
        return null;
    }

    public static Bundle putObject(String key, Bundle bundle, Object o) {
        bundle.putString(key, mapper.toJson(o));
        return bundle;
    }
    public static Intent putObject(String key, Intent intent, Object o) {
        intent.putExtra(key, mapper.toJson(o));
        return intent;
    }
    @Nullable
    public static <T> T getObject(String key, Bundle bundle, Class<T> destinationClass) {
        String serializedObject = bundle.getString(key);
        if (serializedObject != null) {
            return mapper.fromJson(serializedObject, destinationClass);
        }
        return null;
    }
    @Nullable
    public static <T> T getObject(String key, Intent intent, Class<T> destinationClass) {
        String serializedObject = intent.getStringExtra(key);
        if (serializedObject != null) {
            return mapper.fromJson(serializedObject, destinationClass);
        }
        return null;
    }
}
