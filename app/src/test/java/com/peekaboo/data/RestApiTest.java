package com.peekaboo.data;

import android.content.Context;

import com.peekaboo.data.rest.PeekabooApi;
import com.peekaboo.data.rest.RestApi;
import com.peekaboo.data.rest.entity.Credentials;
import com.peekaboo.presentation.di.ApplicationComponent;
import com.peekaboo.presentation.di.ApplicationModule;
import com.peekaboo.presentation.di.DaggerApplicationComponent;
import com.peekaboo.utils.FilesUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.observers.TestSubscriber;

public class RestApiTest {
//    @Mock
//    Context context;
//    @Mock
//    FilesUtils filesUtils;
//    private RestApi restApi;

    @Before
    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        ApplicationComponent applicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(context)).build();
//        PeekabooApi api = applicationComponent.api();
//        restApi = new RestApi(api, context, filesUtils);
    }


    @Test
    public void asd() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("asd");
                subscriber.onCompleted();
            }
        }).filter(s -> !s.equals("asd")).doOnUnsubscribe(new Action0() {
            @Override
            public void call() {
                System.out.println("unsubscribe");
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println(s);
            }
        });
    }
//
//    @Test
//    public void whenSignUpWithValidInputsThenGetCredentials() {
//        TestSubscriber<Credentials> subscriber = new TestSubscriber<>();
//        restApi.signUp(makeValidUser()).subscribe(subscriber);
//
//        subscriber.awaitTerminalEvent();
//        credentials = subscriber.getOnNextEvents().get(0);
//        assertThat(subscriber.getOnNextEvents().size(), is(1));
//    }
}
