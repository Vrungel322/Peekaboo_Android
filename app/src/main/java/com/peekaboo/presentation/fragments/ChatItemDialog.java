package com.peekaboo.presentation.fragments;

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

    private String[] editChatItem;
    private ChatArrayAdapter chatArrayAdapter;
    private int itemIndex;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        itemIndex = args.getInt("index");
    }


//    public static ChatItemDialog newInstance(int index) {
//        ChatItemDialog f = new ChatItemDialog();
//
//        // Supply index input as an argument.
//        Bundle args = new Bundle();
//        args.putInt("index", index);
//        f.setArguments(args);
//
//        return f;
//    }

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
                        break;
                    case 1:
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
