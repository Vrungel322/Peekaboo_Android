package com.peekaboo.data.repositories;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.peekaboo.data.FileEntity;
import com.peekaboo.data.mappers.AbstractMapperFactory;
import com.peekaboo.data.mappers.Mapper;
import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.data.repositories.database.contacts.PContactHelper;
import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.data.repositories.database.messages.PMessageHelper;
import com.peekaboo.data.rest.ConfirmKey;
import com.peekaboo.data.rest.RestApi;
import com.peekaboo.data.rest.entity.ContactEntity;
import com.peekaboo.data.rest.entity.Credentials;
import com.peekaboo.data.rest.entity.CredentialsSignUp;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.domain.Dialog;
import com.peekaboo.domain.Pair;
import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.User;
import com.peekaboo.presentation.pojo.PhoneContactPOJO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Arkadiy on 05.06.2016.
 */
public class SessionDataRepository implements SessionRepository {

    private final AbstractMapperFactory abstractMapperFactory;
    private AccountUser user;
    private RestApi restApi;
    private PContactHelper contactHelper;
    private PMessageHelper messageHelper;
    private ContentResolver contentResolver;

    public SessionDataRepository(RestApi restApi, AbstractMapperFactory abstractMapperFactory,
                                 AccountUser user, PContactHelper dbHelper,
                                 PMessageHelper messageHelper, ContentResolver contentResolver) {
        this.restApi = restApi;
        this.abstractMapperFactory = abstractMapperFactory;
        this.user = user;
        this.contactHelper = dbHelper;
        this.messageHelper = messageHelper;
        this.contentResolver = contentResolver;
    }

    @Override
    public Observable<AccountUser> login(String login, String password) {
        return restApi.login(new Credentials(login, password))
                .map(token -> {
                    user.saveToken(token.getToken());
                    user.saveId(token.getId());
                    user.saveUsername(token.getUsername());
                    user.saveMode(token.getMode());
                    return user;
                }).flatMap(accountUser -> loadAllContacts())
//                .flatMap(contacts -> getPhoneContactList())
                .map(contacts1 -> user);
    }

    @Override
    public Observable<AccountUser> signUp(String phone, String username, String login, String password) {
        return restApi.signUp(new CredentialsSignUp(phone, username, login, password))
                .map(token -> {
                    user.saveId(token.getId());
                    user.saveUsername(token.getUsername());
                    return user;
                });
    }

    @Override
    public Observable<AccountUser> confirm(String id, String key) {
        return restApi.confirm(new ConfirmKey(id, key))
                .map(token -> {
                    user.saveToken(token.getToken());
                    return user;
                }).flatMap(accountUser -> loadAllContacts())
                .map(contacts -> user);
    }

    @Override
    public Observable<User> findFriendByName(String friendName) {
        return restApi.findFriendByName(friendName)
                .map(user -> abstractMapperFactory.getUserMapper().transform(user));
    }

    @Override
    public Call<FileEntity> uploadFile(String fileName, String receiverId) {
        return restApi.uploadFile(fileName, receiverId, user.getBearer());
    }

    @Override
    public Observable<FileEntity> updateAvatar(String fileName) {
        return restApi.updateAvatar(fileName, user.getBearer());
    }


    @Override
    public Call<ResponseBody> downloadFile(String remoteFileName) {
        return restApi.downloadFile(remoteFileName, user.getBearer());
    }

    @Override
    public Observable<List<Contact>> loadAllContacts() {
        Mapper<ContactEntity, Contact> contactEntityMapper = abstractMapperFactory.getContactEntityMapper();
        return restApi.getAllContacts().map(userResponse -> userResponse.usersList)
                .flatMapIterable(l -> l)
                .map(contactEntityMapper::transform)
                .toList()
                .flatMap(this::saveContactToDb);
    }

    @Override
    public Observable<List<Contact>> getAllSavedContacts() {
        return contactHelper.getAllContacts();
    }

    @Override
    public Observable<List<Contact>> saveContactToDb(List<Contact> contact) {
                return Observable.from(contact)
                        .map(contact1 -> {
                            contactHelper.insert(contact1);
                            messageHelper.createTable(contact1.contactId());
                            return contact1;
                        })
                        .toList();
    }

    @Override
    public Observable<List<Dialog>> loadDialogs() {
        return contactHelper.getAllContacts()
                .flatMap(Observable::from)
                .flatMap(contact -> {
                    PMessage message = messageHelper.getLastMessage(contact.contactId());
                    if(message == null){
                        return null;
                    }
                    return Observable.just(new Dialog(contact, message));
                })
                .filter(dialog -> dialog != null)
                .toList();
    }

    @Override
    public Observable<Contact> getContactByContactId(String contactId) {
        return contactHelper.getContactByContactId(contactId);
    }

    @Override
    public Observable<List<PMessage>> getAllUnreadMessages(boolean isMine) {
        return messageHelper.getAllUnreadMessages(isMine);
    }

    @Override
    public Observable<Pair<List<PMessage>, List<Contact>>> getAllUnreadMessagesInfo() {
        return messageHelper.getAllUnreadMessages(false).flatMap(pMessages -> {
            return contactHelper.getContactsForMessages(pMessages);
        }, Pair::new);
    }

    @Override
    public Observable<Integer> getUnreadMessagesCount(String id) {
        return messageHelper.getUnreadMessagesCount(id);
    }

    @Override
    public Observable<List<PhoneContactPOJO>> getPhoneContactList() {
        Cursor phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        List<PhoneContactPOJO> alPhoneContactPOJOs = new ArrayList<PhoneContactPOJO>();
        Set<PhoneContactPOJO> setPhoneContactPOJO = new HashSet<PhoneContactPOJO>();
        return Observable.create(new Observable.OnSubscribe<List<PhoneContactPOJO>>() {
            @Override
            public void call(Subscriber<? super List<PhoneContactPOJO>> subscriber) {

                if (phones != null) {
                    while (phones.moveToNext()) {
                        String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        alPhoneContactPOJOs.add(new PhoneContactPOJO(name, phoneNumber));
                    }
                    phones.close();// close cursor
                }
                subscriber.onNext(alPhoneContactPOJOs);
                subscriber.onCompleted();
            }
        }).distinct().map(phoneContactPOJOs -> {
            setPhoneContactPOJO.addAll(alPhoneContactPOJOs);
            alPhoneContactPOJOs.clear();
            alPhoneContactPOJOs.addAll(setPhoneContactPOJO);
            
            return alPhoneContactPOJOs;

        });

    }
}