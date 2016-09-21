package com.peekaboo.data;

import com.google.gson.Gson;
import com.peekaboo.data.mappers.AbstractMapperFactory;
import com.peekaboo.data.repositories.SessionDataRepository;
import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.data.repositories.database.messages.PMessage;
import com.peekaboo.data.rest.ConfirmKey;
import com.peekaboo.data.rest.RestApi;
import com.peekaboo.data.rest.entity.Credentials;
import com.peekaboo.data.rest.entity.CredentialsSignUp;
import com.peekaboo.data.rest.entity.TokenEntity;
import com.peekaboo.data.rest.entity.UserEntity;
import com.peekaboo.domain.AccountUser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SessionDataRepositoryTest {
    public static final String TOKEN = "token";
    public static final String ID = "id";
    @Mock
    private RestApi restApi;
    @Mock
    private AccountUser user;
    @Mock
    private AbstractMapperFactory mapper;

    private SessionDataRepository sessionDataRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sessionDataRepository = new SessionDataRepository(restApi, mapper, user);
    }

    final BlockingQueue<String> queue = new LinkedBlockingQueue<>();



    @Test
    public void rxQueueTest() {
        System.out.println(Arrays.deepToString("asd".split(" ")));
        ArrayList<UserEntity> userEntities = new ArrayList<>();
        userEntities.add(new UserEntity("1","name1"));
        userEntities.add(new UserEntity("2","name2"));
        userEntities.add(new UserEntity("3","name3"));
        Gson gson = new Gson();
        String s = gson.toJson(userEntities);
        System.out.println(s);
//        System.out.println(Thread.currentThread().hashCode());
//
//        Scheduler from = Schedulers.from(Executors.newSingleThreadExecutor());
//        queue.add("a");
//
//        Subscription complete = Observable.create(new Observable.OnSubscribe<Observable<String>>() {
//            @Override
//            public void call(Subscriber<? super Observable<String>> subscriber) {
//                while (!queue.isEmpty()) {
//                    String poll = queue.poll();
//                    System.out.println(poll + " " + Thread.currentThread().hashCode());
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        System.out.println(e);
//                    }
//                    subscriber.onNext(Observable.just(poll));
//                }
//                subscriber.onCompleted();
//            }
//        }).flatMap(stringObservable -> ).subscribeOn(from)
//                .subscribe(new Subscriber<String>() {
//                    @Override
//                    public void onCompleted() {
//                        System.out.println("complete");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//                        System.out.println("next " + s);
//                    }
//                });
//        queue.add("a1");
////        complete.unsubscribe();
//        queue.add("a2");
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        queue.add("b");
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }


    private Object get() {
        Object o = null;
        return o;
    }


    @Test
    public void rxText() {
        String s1 = (String) get();
        System.out.println(s1);
//        int l1 = 125;
//        System.out.println(String.format("%02d:%02d", l1 / 60, l1 % 60));
        List<String> contacts = new ArrayList<>();
        contacts.add("c1");
        contacts.add("c2");
        contacts.add("c3");

        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);

        List<Integer> list2 = new ArrayList<>();
        list2.add(4);
        list2.add(5);
        list2.add(6);

        List<Integer> list3 = new ArrayList<>();
        list3.add(7);
        list3.add(8);
        list3.add(9);

        Observable.just(contacts)
                .flatMapIterable(l -> l)
//                .switchMap(Observable::from)
                .concatMap(s -> {
                    if (s.contains("1"))
                        return Observable.create(new Observable.OnSubscribe<List<Integer>>() {
                            @Override
                            public void call(Subscriber<? super List<Integer>> subscriber) {
                                subscriber.onNext(list1);
                                subscriber.onCompleted();
                            }
                        });
                    if (s.contains("2"))
                        return Observable.create(new Observable.OnSubscribe<List<Integer>>() {
                            @Override
                            public void call(Subscriber<? super List<Integer>> subscriber) {
                                subscriber.onNext(list2);
                                subscriber.onCompleted();
                            }
                        });
                    if (s.contains("3"))
                        return Observable.create(new Observable.OnSubscribe<List<Integer>>() {
                            @Override
                            public void call(Subscriber<? super List<Integer>> subscriber) {
                                subscriber.onNext(list3);
                                subscriber.onCompleted();
                            }
                        });
                    return Observable.just(new ArrayList<Integer>());
                }).reduce(new ArrayList<>(), (integers, integers2) -> {
            integers.addAll(integers2);
            return integers;
        })
                .subscribe(System.out::println);
    }

    @Test
    public void whenLoginSuccessThenReturnUser() {
        when(restApi.login(any(Credentials.class))).thenReturn(Observable.just(new TokenEntity(TOKEN, ID)));
        TestSubscriber<List<Contact>> subscriber = new TestSubscriber<>();
        sessionDataRepository.login("username", "password").subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnNextEvents().size(), is(1));
        verify(user, times(1)).saveId(ID);
        verify(user, times(1)).saveToken(TOKEN);
    }

    @Test
    public void whenLoginFailureThenReturnError() {
        when(restApi.login(any(Credentials.class))).thenReturn(Observable.create(
                subscriber -> {
                    subscriber.onError(new RuntimeException("Not Great"));
                }));
        TestSubscriber<List<Contact>> subscriber = new TestSubscriber<>();
        sessionDataRepository.login("username", "password").subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnErrorEvents().size(), is(1));
        verify(user, times(0)).saveId(ID);
        verify(user, times(0)).saveToken(TOKEN);
    }

    @Test
    public void whenSignUpSuccessThenReturnUser() {
        when(restApi.signUp(any(CredentialsSignUp.class))).thenReturn(Observable.just(new TokenEntity(null, ID)));
        TestSubscriber<AccountUser> subscriber = new TestSubscriber<>();
        sessionDataRepository.signUp("username", "login", "password").subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnNextEvents().size(), is(1));
        verify(user, times(1)).saveId(ID);
        verify(user, times(0)).saveToken(null);
    }

    @Test
    public void whenSignUpFailureThenReturnError() {
        when(restApi.signUp(any(CredentialsSignUp.class))).thenReturn(Observable.create(
                subscriber -> {
                    subscriber.onError(new RuntimeException("Not Great"));
                }));
        TestSubscriber<AccountUser> subscriber = new TestSubscriber<>();
        sessionDataRepository.signUp("username", "login", "password").subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnErrorEvents().size(), is(1));
        verify(user, times(0)).saveId(ID);
        verify(user, times(0)).saveToken(null);
    }

    @Test
    public void whenConfirmSuccessThenReturnUser() {
        when(restApi.confirm(any(ConfirmKey.class))).thenReturn(Observable.just(new TokenEntity(TOKEN, ID)));
        TestSubscriber<AccountUser> subscriber = new TestSubscriber<>();
        sessionDataRepository.confirm("id", "key").subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnNextEvents().size(), is(1));
        verify(user, times(0)).saveId(ID);
        verify(user, times(1)).saveToken(TOKEN);
    }

    @Test
    public void whenConfirmFailureThenReturnError() {
        when(restApi.confirm(any(ConfirmKey.class))).thenReturn(Observable.create(
                subscriber -> {
                    subscriber.onError(new RuntimeException("Not Great"));
                }));
        TestSubscriber<AccountUser> subscriber = new TestSubscriber<>();
        sessionDataRepository.confirm("id", "key").subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnErrorEvents().size(), is(1));
        verify(user, times(0)).saveId(ID);
        verify(user, times(0)).saveToken(TOKEN);
    }
}
