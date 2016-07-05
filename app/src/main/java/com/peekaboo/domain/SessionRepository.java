package com.peekaboo.domain;


import rx.Observable;

/**
 * Created by Arkadiy on 05.06.2016.
 */
public interface SessionRepository {
    Observable<User> askForUser(String login, String password);
    Observable<User> signUp(String login, String password, String email);
//    Observable<String> getKey();
}
