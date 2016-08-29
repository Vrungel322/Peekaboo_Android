package com.peekaboo.presentation.presenters;

import android.support.annotation.NonNull;

import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.data.repositories.database.messages.PMessageAbs;
import com.peekaboo.data.repositories.database.messages.TextPMessage;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.domain.usecase.FindFriendUseCase;
import com.peekaboo.domain.usecase.Messanger;
import com.peekaboo.presentation.services.INotifier;
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
        INotifier.NotificationListener<PMessage> {
    private Messanger messanger;
    private AccountUser accountUser;
    private CompositeSubscription subscriptions;

    @Inject
    public ChatPresenter2(Messanger messanger, AccountUser accountUser) {
        this.messanger = messanger;
        this.accountUser = accountUser;
    }

    @Override
    public void onResume(boolean isFirstLaunch, String receiver) {
        messanger.tryConnect(accountUser.getBearer());
        messanger.addListener(this);
        subscriptions = new CompositeSubscription();


        if (isFirstLaunch) {
            subscriptions.add(messanger.getAllMessages(receiver)
                    .subscribe(pMessageAbses -> {
                        IChatView2 view = getView();
                        if (view != null) {
                            view.showMessages(pMessageAbses);
                        }
                        subscriptions.unsubscribe();
                    }));
        } else {
            subscriptions.add(messanger.getUnreadMessages(receiver)
                    .subscribe(pMessageAbses -> {
                        IChatView2 view = getView();
                        if (view != null) {
                            view.appendMessages(pMessageAbses);
                        }
                        subscriptions.unsubscribe();
                    }));
        }

    }

    @Override
    public void onPause() {
        messanger.removeListener(this);
        subscriptions.unsubscribe();
    }

    @Override
    public void onSendTextButtonPress(String receiver, String text) {
//        findFriendUseCase.setFriendName(receiver);
//        findFriendUseCase.execute(new BaseUseCaseSubscriber<User>() {
//            @Override
//            public void onNext(User receiver) {
        TextPMessage pMessage = new TextPMessage(Utility.getPackageId(),
                true, text, System.currentTimeMillis(),
                PMessageAbs.PMESSAGE_STATUS.STATUS_SENT,
                receiver, accountUser.getId());
        messanger.sendMessage(pMessage);
//            }
//        });
    }

    @Override
    public void onUserMessageRead(PMessage message) {
        messanger.readMessage(message);
    }

    @Override
    public void onMessageRead(PMessage message) {
        IChatView2 view = getView();
        if (view != null) {
            view.updateMessage(message);
        }
    }

    @Override
    public void onMessageObtained(PMessage message) {
        IChatView2 view = getView();
        if (view != null) {
            view.appendMessages(messageToList(message));
        }
    }

    @Override
    public boolean willHandleMessage(PMessage message) {
        IChatView2 view = getView();
        boolean mustBeShow = false;
        if (view != null) {
            mustBeShow = message.senderId().equals(view.getCompanionId());
        }
        return mustBeShow;
    }

    @Override
    public void onMessageSent(PMessage message) {
        IChatView2 view = getView();
        if (view != null) {
            view.appendMessages(messageToList(message));
        }
    }

    @NonNull
    private ArrayList<PMessageAbs> messageToList(PMessageAbs message) {
        ArrayList<PMessageAbs> messageList = new ArrayList<>();
        messageList.add(message);
        return messageList;
    }

}
