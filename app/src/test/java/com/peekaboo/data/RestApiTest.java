package com.peekaboo.data;

import android.content.Context;
import android.os.Parcel;

import com.peekaboo.data.rest.PeekabooApi;
import com.peekaboo.data.rest.RestApi;
import com.peekaboo.data.rest.entity.Credentials;
import com.peekaboo.presentation.di.ApplicationComponent;
import com.peekaboo.presentation.di.ApplicationModule;
import com.peekaboo.presentation.di.DaggerApplicationComponent;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;

import rx.observers.TestSubscriber;

public class RestApiTest {
//    @Mock
//    Context context;
//    private RestApi restApi;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
//        ApplicationComponent applicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(context)).build();
//        PeekabooApi api = applicationComponent.api();
//        restApi = new RestApi(api);
    }

    @Test
    public void asd() {
        Assert.assertEquals(true, true);
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
