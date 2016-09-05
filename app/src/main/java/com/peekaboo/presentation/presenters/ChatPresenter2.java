package com.peekaboo.presentation.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.data.repositories.database.messages.PMessageAbs;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.presentation.services.IMessenger;
import com.peekaboo.presentation.views.IChatView2;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by sebastian on 24.08.16.
 */
@Singleton
public class ChatPresenter2 extends BasePresenter<IChatView2> implements IChatPresenter2<IChatView2>,
        IMessenger.MessengerListener {
    private IMessenger messenger;
    private AccountUser accountUser;
    private CompositeSubscription subscriptions;

    @Inject
    public ChatPresenter2(IMessenger messenger, AccountUser accountUser) {
        this.messenger = messenger;
        this.accountUser = accountUser;
    }

    @Override
    public void onResume(boolean isFirstLaunch, String receiver) {
        messenger.tryConnect(accountUser.getBearer());
        messenger.addMessageListener(this);

        subscriptions = new CompositeSubscription();

        if (isFirstLaunch) {
            subscriptions.add(messenger.getAllMessages(receiver)
                    .subscribe(pMessageAbses -> {
                        subscriptions.unsubscribe();
                        IChatView2 view = getView();
                        if (view != null) {
                            view.showMessages(pMessageAbses);
                        }
                    }));
        } else {
            Log.e("BUG", "onResume");
            subscriptions.add(messenger.getUnreadMessages(receiver)
                    .subscribe(pMessageAbses -> {
                        Log.e("BUG", "unsubscribe");
                        subscriptions.unsubscribe();
                        IChatView2 view = getView();
                        if (view != null) {
                            view.appendMessages(pMessageAbses);
                        }
                    }));
        }

    }

    @Override
    public void onPause() {
        messenger.removeMessageListener(this);
        subscriptions.unsubscribe();
    }

    @Override
    public void onSendTextButtonPress(String receiver, String text) {
        PMessage pMessage = new PMessage(
                true, PMessageAbs.PMESSAGE_MEDIA_TYPE.TEXT_MESSAGE, text, System.currentTimeMillis(),
                PMessageAbs.PMESSAGE_STATUS.STATUS_SENT,
                receiver, accountUser.getId());
        messenger.sendMessage(pMessage);
    }

    @Override
    public void onUserMessageRead(PMessage message) {
        messenger.readMessage(message);
    }

    @Override
    public void onMessageRead(PMessage message) {
        IChatView2 view = getView();
        if (view != null && isFromCurrentChat(message, view)) {
            view.updateMessage(message);
        }
    }


    private boolean isFromCurrentChat(@NonNull PMessage message, @NonNull IChatView2 view) {
        return (message.senderId().equals(view.getCompanionId()) && !message.isMine())
                || (message.receiverId().equals(view.getCompanionId()) && message.isMine());
    }

    @Override
    public void onMessageDelivered(PMessage message) {
        IChatView2 view = getView();
        if (view != null && isFromCurrentChat(message, view)) {
            view.updateMessage(message);
        }
    }

    @Override
    public int willChangeStatus(PMessage message) {
        IChatView2 view = getView();
        return view != null && isFromCurrentChat(message, view) ?
                PMessageAbs.PMESSAGE_STATUS.STATUS_READ
                : message.status();
    }

    @Override
    public void onMessageSent(PMessage message) {
        IChatView2 view = getView();
        if (view != null && isFromCurrentChat(message, view)) {
            view.appendMessages(messageToList(message));
        }
    }

    @NonNull
    private ArrayList<PMessage> messageToList(PMessage message) {
        ArrayList<PMessage> messageList = new ArrayList<>();
        messageList.add(message);
        return messageList;
    }
}
