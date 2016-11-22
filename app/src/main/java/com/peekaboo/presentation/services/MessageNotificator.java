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

import com.peekaboo.R;
import com.peekaboo.data.di.scope.UserScope;
import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.domain.Pair;
import com.peekaboo.domain.subscribers.BaseUseCaseSubscriber;
import com.peekaboo.domain.usecase.GetAllUnreadMessagesInfoUseCase;
import com.peekaboo.presentation.activities.MainActivity;
import com.peekaboo.presentation.utils.ResourcesUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by arkadius on 10/5/16.
 */
@UserScope
public class MessageNotificator {

    public static final int NOTIFICATION_ID = 101;
    private static final int ON_MS = 500;
    private static final int OFF_MS = 2000;
    private static final int ARGB = Color.CYAN;
    private final NotificationManager notificationManager;
    private final GetAllUnreadMessagesInfoUseCase getAllUnreadMessagesInfoUseCase;
    private final Picasso picasso;
    private final Uri ringtoneUri;
    private final int avatarSize;
    private Context context;

    @Inject
    public MessageNotificator(Context context,
                              GetAllUnreadMessagesInfoUseCase getAllUnreadMessagesInfoUseCase,
                              Picasso picasso) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.getAllUnreadMessagesInfoUseCase = getAllUnreadMessagesInfoUseCase;
        this.picasso = picasso;
        ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        avatarSize = ResourcesUtils.getDimenInPx(context, R.dimen.notification_avatar_size);
    }

    private void showNotification(List<PMessage> messages, List<Contact> contacts, PMessage message) {
        Contact currentContact = null;
        for (Contact contact : contacts) {
            if (message.senderId().equals(contact.contactId())) {
                currentContact = contact;
                break;
            }
        }
        if (currentContact != null && !messages.isEmpty() && !contacts.isEmpty()) {
            String ticker = currentContact.contactNickname() + " - " + context.getString(R.string.newMessage);

            if (messages.size() == 1) {
                showSinglePersonNotification(ticker, message.messageBody(), currentContact);
            } else if (fromSinglePerson(messages)) {
                String text = messages.size() + " " + context.getString(R.string.newMessages);
                showSinglePersonNotification(ticker, text, currentContact);
            } else {
                String text = messages.size() + " " + context.getString(R.string.newMessages);
                String title = constructTitleForMultipleContacts(contacts);
                showNotification(ticker, title, text);
            }
        }
    }

    private String constructTitleForMultipleContacts(List<Contact> contacts) {
        String title = "";
        for (Contact contact : contacts) {
            title += contact.contactNickname() + ", ";
        }
        title = title.substring(0, title.length() - 2);
        return title;
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
        notificationManager.notify(NOTIFICATION_ID, getNotification(ticker, contact.contactNickname(), message, resultPendingIntent));
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

    public void onMessageObtained(final PMessage message) {
        if (!message.isMine() && message.status() == PMessage.PMESSAGE_STATUS.STATUS_DELIVERED) {
            Log.e("Messenger", "onMessageObtained " + message);
            getAllUnreadMessagesInfoUseCase.execute(new BaseUseCaseSubscriber<Pair<List<PMessage>, List<Contact>>>() {
                @Override
                public void onNext(final Pair<List<PMessage>, List<Contact>> pair) {
                    showNotification(pair.first, pair.second, message);
                }
            });
        }
    }
}
