package com.peekaboo.presentation.listeners;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import com.peekaboo.presentation.fragments.ChatItemDialog;
import com.peekaboo.utils.Constants;

/**
 * Created by st1ch on 17.08.2016.
 */
public class ChatOnClickListener implements ChatClickListener {

    private Activity activity;

    public ChatOnClickListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View view, int position) {

    }

    @Override
    public void onLongClick(View view, int position) {
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        ChatItemDialog chatItemDialog = new ChatItemDialog();
        Bundle itemIndexBundle = new Bundle();
        itemIndexBundle.putInt(Constants.ARG_CHAT_MESSAGE_ITEM_INDEX, position);
        chatItemDialog.setArguments(itemIndexBundle);
//        chatItemDialog.show(ft, Constants.FRAGMENT_TAGS.CHAT_ITEM_DIALOG_FRAGMENT_TAG);
    }
}
