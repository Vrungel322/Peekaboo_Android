package com.peekaboo.presentation.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.peekaboo.R;
import com.peekaboo.presentation.activities.ChatActivity;
import com.peekaboo.presentation.adapters.ChatArrayAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nataliia on 18.07.2016.
 */

public class ChatItemDialog extends DialogFragment{
    @BindView(R.id.chat_item_list)
    ListView lvChatItem;

    public interface onSomeEventListener {
        public void copyText(int index);
        public void deleteMess(int index);
    }

    onSomeEventListener someEventListener;

    private String[] editChatItem;

    private ChatArrayAdapter chatArrayAdapter;
    private int itemIndex;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        itemIndex = args.getInt("index");
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            someEventListener = (onSomeEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        editChatItem = getResources().getStringArray(R.array.edit_chat_item);

        itemIndex = getArguments().getInt("index");
        View view = inflater.inflate(R.layout.chat_item_dialog, container, false);
        ButterKnife.bind(this, view);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), R.layout.attach_dialog_item, editChatItem);

        lvChatItem.setAdapter(adapter);
        lvChatItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case 0:
                        someEventListener.copyText(itemIndex);
                        dismiss();
                        break;
                    case 1:
                        someEventListener.deleteMess(itemIndex);
                        dismiss();
                        break;
                    case 2:
                        Toast.makeText(getActivity(), "2", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(getActivity(), "3", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        return view;
    }

}
