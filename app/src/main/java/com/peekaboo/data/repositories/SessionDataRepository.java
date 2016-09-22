package com.peekaboo.data.repositories;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.peekaboo.data.FileEntity;
import com.peekaboo.data.mappers.AbstractMapperFactory;
import com.peekaboo.data.mappers.Mapper;
import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.data.repositories.database.contacts.PContactHelper;
import com.peekaboo.data.rest.ConfirmKey;
import com.peekaboo.data.rest.RestApi;
import com.peekaboo.data.rest.entity.Credentials;
import com.peekaboo.data.rest.entity.CredentialsSignUp;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.data.rest.entity.ContactEntity;
import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.User;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Arkadiy on 05.06.2016.
 */
public class SessionDataRepository implements SessionRepository {

    private final AbstractMapperFactory abstractMapperFactory;
    private AccountUser user;
    private PContactHelper contactHelper;
    private RestApi restApi;

    public SessionDataRepository(RestApi restApi, AbstractMapperFactory abstractMapperFactory, AccountUser user, PContactHelper contactHelper) {
        this.restApi = restApi;
        this.abstractMapperFactory = abstractMapperFactory;
        this.user = user;
        this.contactHelper = contactHelper;
    }

    @Override
    public Observable<List<Contact>> login(String login, String password) {
        return restApi.login(new Credentials(login, password))
                .map(token -> {
                    user.saveToken(token.getToken());
                    user.saveId(token.getId());
                    return user;
                }).flatMap(accountUser -> loadAllContacts());
    }

    @Override
    public Observable<AccountUser> signUp(String username, String login, String password) {
        return restApi.signUp(new CredentialsSignUp(username, login, password))
                .map(token -> {
                    user.saveId(token.getId());
                    return user;
                });
    }

    @Override
    public Observable<AccountUser> confirm(String id, String key) {
        return restApi.confirm(new ConfirmKey(id, key))
                .map(token -> {
                    user.saveToken(token.getToken());
                    return user;
                });
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
    public Call<ResponseBody> downloadFile(String remoteFileName) {
        return restApi.downloadFile(remoteFileName, user.getBearer());
    }

    @Override
    public Observable<List<Contact>> loadAllContacts() {
        Mapper<ContactEntity, Contact> contactEntityMapper = abstractMapperFactory.getContactEntityMapper();
        return restApi.getAllContacts().map(userResponse -> userResponse.usersList)
                .flatMapIterable(l -> l)
                .map(contactEntity -> {
                    Contact contact = contactEntityMapper.transform(contactEntity);
//                    contactHelper.insert(contact);
                    return contact;
                })
                .toList();
    }

    @Override
    public Observable<List<Contact>> loadAllContactsFromDb() {
        return contactHelper.getAllContacts();
    }
}