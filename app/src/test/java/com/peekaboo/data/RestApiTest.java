package com.peekaboo.data;

import android.content.Context;

import com.peekaboo.data.rest.PeekabooApi;
import com.peekaboo.data.rest.RestApi;
import com.peekaboo.data.rest.entity.Credentials;
import com.peekaboo.presentation.di.ApplicationComponent;
import com.peekaboo.presentation.di.ApplicationModule;
import com.peekaboo.presentation.di.DaggerApplicationComponent;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.observers.TestSubscriber;

public class RestApiTest {
    @Mock
    Context context;
    private RestApi restApi;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ApplicationComponent applicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(context)).build();
        PeekabooApi api = applicationComponent.api();
        restApi = new RestApi(api, context);
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
