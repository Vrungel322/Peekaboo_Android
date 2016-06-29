package com.peekaboo.presentation.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.peekaboo.R;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.di.ApplicationComponent;
import com.peekaboo.presentation.presenters.MainActivityPresenter;
import com.peekaboo.utils.ActivityNavigator;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Inject
    ActivityNavigator mNavigator;
    @Inject
    MainActivityPresenter mainActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ButterKnife.bind(this);
        ApplicationComponent component = PeekabooApplication.getApp(this).getComponent();
        component.inject(this);
        //запускает LodInActivity
        mNavigator.startLogInActivity(this);
    }
}
