package com.peekaboo.presentation.fragments;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.peekaboo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nataliia on 12.07.2016.
 */
public class AttachmentChatDialog  extends DialogFragment {
    public static int REQUEST_CODE_CAMERA = 1818;
    public static int REQUEST_CODE_GALERY = 1819;

    @BindView(R.id.attach_list)
    ListView lvAttachments;

    private String[] attach_list_strings;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        attach_list_strings = getResources().getStringArray(R.array.attacment_list);

        View view = inflater.inflate(R.layout.attach_dialog, container, false);
        ButterKnife.bind(this, view);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), R.layout.attach_dialog_item, attach_list_strings);

        lvAttachments.setAdapter(adapter);
        lvAttachments.setOnItemClickListener((adapterView, view1, i, l) -> {
            switch(i){
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
        });
        return view;
    }

    private void takeGaleryImage() {
        Intent intent = new Intent();
        intent.setType("image/*"); // to open gallery
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getActivity().startActivityForResult(Intent.createChooser(intent, getActivity().getString(R.string.selectImage)), REQUEST_CODE_GALERY);
    }

    public void takePhoto() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        getActivity().startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
    }
}
