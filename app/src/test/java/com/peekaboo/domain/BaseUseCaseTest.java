package com.peekaboo.domain;

import android.util.Log;

import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import rx.schedulers.Schedulers;

import static org.mockito.Mockito.when;

/**
 * Created by sebastian on 29.06.16.
 */
@PrepareForTest(android.util.Log.class)

public class BaseUseCaseTest {
    @Mock
    protected ObserveOn observeOn;
    @Mock
    protected SubscribeOn subscribeOn;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(Log.class);

        when(observeOn.getScheduler()).thenReturn(Schedulers.newThread());
        when(subscribeOn.getScheduler()).thenReturn(Schedulers.newThread());
    }
}
