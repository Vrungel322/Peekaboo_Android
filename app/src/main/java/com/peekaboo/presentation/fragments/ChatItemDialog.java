package com.peekaboo.presentation.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.peekaboo.R;
import com.peekaboo.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nataliia on 18.07.2016.
 */

public class ChatItemDialog extends android.support.v4.app.DialogFragment {
    @BindView(R.id.chat_item_list)
    ListView lvChatItem;

    public interface IChatItemEventListener {
        void copyText(int index);
        void deleteMess(int index);
        void textToSpeech(int index);
    }

    IChatItemEventListener chatItemEventListener;

    private String[] editChatItem;

    private int itemIndex;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        itemIndex = args.getInt(Constants.ARG_CHAT_MESSAGE_ITEM_INDEX);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setStyle(STYLE_NO_TITLE,0);
    }

    public void setChatItemEventListener(IChatItemEventListener iChatItemEventListener){
        this.chatItemEventListener = iChatItemEventListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        editChatItem = getResources().getStringArray(R.array.edit_chat_item);
        View view = inflater.inflate(R.layout.chat_item_dialog, container, false);

        ButterKnife.bind(this, view);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), R.layout.attach_dialog_item, editChatItem);

        lvChatItem.setAdapter(adapter);
        lvChatItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        chatItemEventListener.copyText(itemIndex);
                        break;
                    case 1:
                        chatItemEventListener.deleteMess(itemIndex);
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        chatItemEventListener.textToSpeech(itemIndex);
                        break;
                }
                dismiss();
            }
        });
        return view;
    }

}
