package com.peekaboo.presentation;

import com.peekaboo.domain.schedulers.ObserveOn;
import com.peekaboo.domain.schedulers.SubscribeOn;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.schedulers.Schedulers;

import static org.mockito.Mockito.when;

/**
 * Created by sebastian on 16.07.16.
 */
public class BasePresenterTest {
    protected final static int WAIT = 50;
    @Mock protected ObserveOn observeOn;
    @Mock
    protected SubscribeOn subscribeOn;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(observeOn.getScheduler()).thenReturn(Schedulers.newThread());
        when(subscribeOn.getScheduler()).thenReturn(Schedulers.newThread());
    }
}
