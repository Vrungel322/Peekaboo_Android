package com.peekaboo.presentation.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.widget.ListView;
import android.widget.Toast;

import com.peekaboo.R;

import butterknife.BindView;

/**
 * Created by Nataliia on 12.07.2016.
 */
public class AttachmentChatDialog extends DialogFragment {
    public static int REQUEST_CODE_CAMERA = 1818;
    public static int REQUEST_CODE_GALERY = 1819;

    @BindView(R.id.attach_list)
    ListView lvAttachments;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setItems(R.array.attacment_list, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            takePhoto();
                            break;
                        case 1:
                            takeGaleryImage();
                            break;
                        case 2:
                            Toast.makeText(getActivity().getApplicationContext(), "2", Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            Toast.makeText(getActivity().getApplicationContext(), "3", Toast.LENGTH_SHORT).show();
                            break;
                    }
                })
                .create();
    }

    private void takeGaleryImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getActivity().startActivityForResult(Intent.createChooser(intent, getActivity().getString(R.string.selectImage)), REQUEST_CODE_GALERY);
    }

    public void takePhoto() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        getActivity().startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
    }


    public interface IAttachmentDialogEventListener {
        void takeGalleryImage();
        void takePhoto();
        void takeAudio();
        void takeDocument();
        void takeSpeech();
    }

}
