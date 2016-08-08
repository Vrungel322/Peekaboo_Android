package com.peekaboo.presentation.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.peekaboo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nataliia on 12.07.2016.
 */
public class AttachmentChatDialog  extends DialogFragment {
    @BindView(R.id.attach_list)
    ListView lvAttachments;

    public interface IAttachmentDialogEventListener {
        void takeGalleryImage();
        void takePhoto();
        void takeAudio();
        void takeDocument();
        void takeSpeech_11025();
        void takeSpeech_16000();
        void takeSpeech_22050();
        void takeSpeech_32000();
    }

    IAttachmentDialogEventListener attachmentDialogEventListener;

    private String[] attach_list_strings;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        attachmentDialogEventListener = (IAttachmentDialogEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }

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
                    attachmentDialogEventListener.takePhoto();
                    break;
                case 1:
                    attachmentDialogEventListener.takeGalleryImage();
                    break;
                case 2:
                    attachmentDialogEventListener.takeAudio();
                    break;
                case 3:
                    attachmentDialogEventListener.takeDocument();
                    break;
                case 4:
                    attachmentDialogEventListener.takeSpeech_11025();
                    break;
                case 5:
                    attachmentDialogEventListener.takeSpeech_16000();
                    break;
                case 6:
                    attachmentDialogEventListener.takeSpeech_22050();
                    break;
                case 7:
                    attachmentDialogEventListener.takeSpeech_32000();
                    break;
            }
        });
        return view;
    }
}
