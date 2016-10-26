package com.peekaboo.domain;


import com.peekaboo.data.FileEntity;
import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.presentation.pojo.PhoneContactPOJO;
import com.peekaboo.data.repositories.database.messages.PMessage;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import rx.Observable;

/**
 * Created by Arkadiy on 05.06.2016.
 */
public interface SessionRepository {
    Observable<AccountUser> login(String login, String password);

    Observable<AccountUser> signUp(String phone, String username, String login, String password);

    Observable<AccountUser> confirm(String id, String key);

    Observable<User> findFriendByName(String friendName);

    Call<FileEntity> uploadFile(String fileType, String fileName, String receiverId);

    Observable<FileEntity> updateAvatar(String fileName);

    Call<ResponseBody> downloadFile(String remoteFileName, String fileType);

    Observable<List<Contact>> loadAllContacts();

    Observable<List<Contact>> getAllSavedContacts();

    Observable saveContactToDb(List<Contact> contact);

    Observable<List<Dialog>> loadDialogs();

    Observable<Contact> getContactByContactId(String contactId);

    Observable<List<PMessage>> getAllUnreadMessages(boolean isMine);

    Observable<Pair<List<PMessage>,List<Contact>>> getAllUnreadMessagesInfo();

    Observable<Integer> getUnreadMessagesCount(String id);

    Observable<List<Sms>> getAllSmsList();

    Observable<List<Sms>> getContactSmsList(String phoneNumber);

    Observable<Sms> getContactLastSms(String phoneNumber);

    Observable<List<SmsDialog>> getSmsDialogsList();

    Observable<SmsDialog> getSmsDialogs();

    Observable<Integer> getSmsContactUnreadMessagesCount(String phoneNumber);

    Observable<PhoneContactPOJO> getPhoneContacts();

    Observable<List<PhoneContactPOJO>> getPhoneContactList();
}
