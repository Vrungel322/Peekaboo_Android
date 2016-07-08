package com.peekaboo.domain;


import rx.Observable;

/**
 * Created by Arkadiy on 05.06.2016.
 */
public interface SessionRepository {
    Observable<User> login(String login, String password);
    Observable<User> signUp(String username, String login, String password);
    Observable<User> confirm(String id, String key);
}
