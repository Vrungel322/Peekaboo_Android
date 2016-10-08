package com.peekaboo.presentation.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.peekaboo.R;
import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.domain.Pair;
import com.peekaboo.domain.subscribers.BaseUseCaseSubscriber;
import com.peekaboo.domain.usecase.GetAllUnreadMessagesInfoUseCase;
import com.peekaboo.domain.usecase.GetContactByContactIdUseCase;
import com.peekaboo.presentation.activities.MainActivity;
import com.peekaboo.presentation.utils.ResourcesUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by arkadius on 10/5/16.
 */
@Singleton
public class MessageNotificator implements IMessenger.MessengerListener {

    public static final int NOTIFICATION_ID = 101;
    public static final int ON_MS = 500;
    public static final int OFF_MS = 2000;
    public static final int ARGB = Color.CYAN;
    private final NotificationManager notificationManager;
    private final GetAllUnreadMessagesInfoUseCase getAllUnreadMessagesInfoUseCase;
    private final Picasso picasso;
    private final Uri ringtoneUri;
    private final int avatarSize;
    private Context context;

    @Inject
    public MessageNotificator(Context context, IMessenger messenger,
                              GetAllUnreadMessagesInfoUseCase getAllUnreadMessagesInfoUseCase,
                              Picasso picasso) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.getAllUnreadMessagesInfoUseCase = getAllUnreadMessagesInfoUseCase;
        this.picasso = picasso;
        ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        avatarSize = ResourcesUtils.getDimenInPx(context, R.dimen.notification_avatar_size);

        messenger.addMessageListener(this);
    }

    @Override
    public void onMessageUpdated(final PMessage message) {
        Log.e("MessageNotificator", "onMessageUpdate " + message);
        if (!message.isMine() && message.status() == PMessage.PMESSAGE_STATUS.STATUS_DELIVERED) {
            getAllUnreadMessagesInfoUseCase.execute(new BaseUseCaseSubscriber<Pair<List<PMessage>, List<Contact>>>() {
                @Override
                public void onNext(final Pair<List<PMessage>, List<Contact>> pair) {
                    List<PMessage> messages = pair.first;
                    List<Contact> contacts = pair.second;
                    Log.e("MessageNotificator", "onNext " + messages.size() + " " + contacts.size());
                    Contact currentContact = null;
                    for (Contact contact : contacts) {
                        if (message.senderId().equals(contact.contactId())) {
                            currentContact = contact;
                            break;
                        }
                    }
                    if (currentContact != null && !messages.isEmpty() && !contacts.isEmpty()) {
                        Log.e("MessageNotificator", "everything is good " + currentContact);
                        String ticker = currentContact.contactNickname() + " - " + context.getString(R.string.newMessage);

                        if (messages.size() == 1) {
                            showSinglePersonNotification(ticker, message.messageBody(), currentContact);
                        } else if (fromSinglePerson(messages)) {
                            String text = messages.size() + " " + context.getString(R.string.newMessages);
                            showSinglePersonNotification(ticker, text, currentContact);
                        } else {
                            String text = messages.size() + " " + context.getString(R.string.newMessages);
                            String title = "";
                            for (Contact contact : contacts) {
                                title += contact.contactNickname() + ", ";
                            }
                            title = title.substring(0, title.length() - 2);
                            showNotification(ticker, title, text);
                        }
                    }
                }

            });
        }

    }

    private boolean fromSinglePerson(List<PMessage> messages) {
        boolean result = false;
        String currentId = null;
        for (PMessage message : messages) {
            if (currentId == null) {
                currentId = message.senderId();
            }
            result = message.senderId().equals(currentId);
            currentId = message.senderId();
            if (!result) {
                break;
            }
        }
        return result;
    }

    private void showNotification(String ticker, String title, String message) {
        Intent resultIntent = new Intent(context, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .setAction(MainActivity.ACTION.SHOW_DIALOGS);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationManager.notify(NOTIFICATION_ID, getNotification(ticker, title, message, resultPendingIntent));
    }

    private void showSinglePersonNotification(String ticker, String message, Contact contact) {
        Intent resultIntent = new Intent(context, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .setAction(MainActivity.ACTION.SHOW_CHAT)
                .putExtra(MainActivity.ACTION.EXTRA.CONTACT_EXTRA, contact);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        final RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.notification_widget);
        remoteView.setTextViewText(R.id.tvAuthor, contact.contactNickname());
        remoteView.setTextViewText(R.id.tvMessage, message);

        notificationManager.notify(NOTIFICATION_ID, getNotification(ticker, resultPendingIntent, remoteView));

        picasso.load(contact.contactImgUri())
                .resize(avatarSize, 0)
                .into(remoteView, R.id.ivAvatar, NOTIFICATION_ID, getNotification(resultPendingIntent, remoteView));
    }

    private Notification getNotification(String ticker, PendingIntent resultPendingIntent, RemoteViews remoteView) {
        return new NotificationCompat.Builder(context)
                .setTicker(ticker)
                .setSmallIcon(R.drawable.ic_message_white_24dp)
                .setContent(remoteView)
                .setContentIntent(resultPendingIntent)
                .setSound(ringtoneUri)
//                .setColor(Color.GREEN)
                .setLights(ARGB, ON_MS, OFF_MS)
                .build();
    }

    private Notification getNotification(PendingIntent resultPendingIntent, RemoteViews remoteView) {
        return new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_message_white_24dp)
                .setContent(remoteView)
                .setContentIntent(resultPendingIntent)
                .setLights(ARGB, ON_MS, OFF_MS)
                .build();
    }

    private Notification getNotification(String ticker, String title, String message, PendingIntent resultPendingIntent) {
        return new NotificationCompat.Builder(context)
                .setTicker(ticker)
                .setSmallIcon(R.drawable.ic_message_white_24dp)
                .setContentText(message)
                .setContentTitle(title)
                .setLights(ARGB, ON_MS, OFF_MS)
                .setSound(ringtoneUri)
                .setContentIntent(resultPendingIntent)
                .build();
    }

    @Override
    public int willChangeStatus(PMessage message) {
        return message.status();
    }
}
