package com.peekaboo.presentation.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.data.repositories.database.messages.PMessageAbs;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.presentation.services.IMessenger;
import com.peekaboo.presentation.views.IChatView2;
import com.peekaboo.utils.Utility;

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
    private IMessenger messanger;
    private AccountUser accountUser;
    private CompositeSubscription subscriptions;

    @Inject
    public ChatPresenter2(IMessenger messanger, AccountUser accountUser) {
        this.messanger = messanger;
        this.accountUser = accountUser;
    }

    @Override
    public void onResume(boolean isFirstLaunch, String receiver) {
        messanger.tryConnect(accountUser.getBearer());
        messanger.addMessageListener(this);

        subscriptions = new CompositeSubscription();

        if (isFirstLaunch) {
            subscriptions.add(messanger.getAllMessages(receiver)
                    .subscribe(pMessageAbses -> {
                        subscriptions.unsubscribe();
                        IChatView2 view = getView();
                        if (view != null) {
                            view.showMessages(pMessageAbses);
                        }
                    }));
        } else {
            subscriptions.add(messanger.getUnreadMessages(receiver)
                    .subscribe(pMessageAbses -> {
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
        messanger.removeMessageListener(this);
        subscriptions.unsubscribe();
    }

    @Override
    public void onSendTextButtonPress(String receiver, String text) {
        PMessage pMessage = new PMessage(Utility.getPackageId(),
                true, PMessageAbs.PMESSAGE_MEDIA_TYPE.TEXT_MESSAGE, text, System.currentTimeMillis(),
                PMessageAbs.PMESSAGE_STATUS.STATUS_SENT,
                receiver, accountUser.getId());
        messanger.sendMessage(pMessage);
    }

    @Override
    public void onUserMessageRead(PMessage message) {
        messanger.readMessage(message);
    }

    @Override
    public void onMessageRead(PMessage message) {
        boolean mustBeShown = isMustBeShown(message);
        Log.e("read notification", "must show " + mustBeShown);
        IChatView2 view = getView();
        if (view != null) {
            if (message.receiverId().equals(view.getCompanionId())
                    || message.senderId().equals(view.getCompanionId())) {
                view.updateMessage(message);
            }
        }
    }

    private boolean isMustBeShown(PMessage message) {
        IChatView2 view = getView();
        boolean mustBeShown = false;
        if (view != null) {
            Log.e("message", message.senderId() + " " + view.getCompanionId() + " " + accountUser.getId());
            mustBeShown = message.senderId().equals(view.getCompanionId());
        }
        return mustBeShown;
    }

    @Override
    public void onMessageDelivered(PMessage message) {
        if (isMustBeShown(message)) {
            IChatView2 view = getView();
            if (view != null) {
                view.updateMessage(message);
            }
        }
    }

    @Override
    public int willChangeStatus(PMessage message) {
        return isMustBeShown(message) ? PMessageAbs.PMESSAGE_STATUS.STATUS_READ : message.status();
    }

    @Override
    public void onMessageSent(PMessage message) {
        IChatView2 view = getView();
        if (view != null) {
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
