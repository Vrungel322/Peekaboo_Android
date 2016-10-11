package com.peekaboo.presentation.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.peekaboo.R;
import com.peekaboo.presentation.PeekabooApplication;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by Nataliia on 04.10.2016.
 */

public class AvatarChangeDialog extends DialogFragment {

    @BindView(R.id.ava_change)
    ListView avaChange;

    private String[] dialogItem;

    public interface IAvatarChangeListener {
        void takePhoto();
        void takeFromGallery();
    }

    IAvatarChangeListener iAvatarChangeListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setStyle(STYLE_NO_TITLE,0);
        try {
            iAvatarChangeListener = (IAvatarChangeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dialogItem = getResources().getStringArray(R.array.avatar_change_dialog);
        PeekabooApplication.getApp(getActivity()).getComponent().inject(this);
        View view = inflater.inflate(R.layout.avatar_change_dialog, container, false);
        ButterKnife.bind(this, view);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), R.layout.attach_dialog_item, dialogItem);
        avaChange.setAdapter(adapter);
        return view;
    }

    @OnItemClick(R.id.ava_change)
    void onItemSelected(int position) {
        if(position == 0){
            iAvatarChangeListener.takePhoto();
        }else{
            iAvatarChangeListener.takeFromGallery();
        }
        dismiss();
    }
}
