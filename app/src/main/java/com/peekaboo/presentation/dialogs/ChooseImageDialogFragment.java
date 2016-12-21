package com.peekaboo.presentation.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.peekaboo.R;
import com.peekaboo.utils.IntentUtils;


/**
 * Created by sebastian on 08.07.16.
 */
public class ChooseImageDialogFragment extends DialogFragment {

    public static final String TAG = "choose_dialog";
    public static final String IMAGE_PATH = "image_path";
    private String file;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            file = savedInstanceState.getString(IMAGE_PATH);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_choose_picture, null);

        view.findViewById(R.id.item_camera).setOnClickListener(v -> {
            captureImage();
        });
        view.findViewById(R.id.item_gallery).setOnClickListener(v -> {
            takeGallery();
        });

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IntentUtils.CAMERA_REQUEST_CODE_PHOTO:
            case IntentUtils.GALLERY_REQUEST_CODE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    if (file == null) {
                        file = IntentUtils.onGalleryActivityResult(getActivity(), requestCode, resultCode, data);
                    }
                    if (file != null) {
                        dismissAllowingStateLoss();
                        ChooseImageListener chooseImageListener = (ChooseImageListener) getActivity();
                        if (chooseImageListener != null) {
                            chooseImageListener.onImageChosen(file);
                        }
                    }
                } else {
                    file = null;
                }
                break;
        }
    }

    private void showToast(String message) {
        Toast.makeText(
                getActivity().getApplicationContext(),
                message,
                Toast.LENGTH_SHORT
        ).show();
    }

    private void takeGallery() {
        boolean canTake = IntentUtils.takeGalleryImage(this);
        if (!canTake) {
            showToast(getString(R.string.galleryIsNotAvailable));
        }
    }

    private void captureImage() {
        file = IntentUtils.capturePhoto(this);
        if (file == null) {
            showToast(getString(R.string.cameraIsNotAvailable));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(IMAGE_PATH, file);
        super.onSaveInstanceState(outState);
    }

    public interface ChooseImageListener {
        void onImageChosen(String file);
    }
}
