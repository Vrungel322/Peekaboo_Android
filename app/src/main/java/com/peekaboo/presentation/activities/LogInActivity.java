package com.peekaboo.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.peekaboo.R;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.fragments.ProgressDialogFragment;
import com.peekaboo.presentation.presenters.LoginPresenter;
import com.peekaboo.presentation.views.ICredentialsView;
import com.peekaboo.utils.ActivityNavigator;
import com.squareup.otto.Bus;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogInActivity extends AppCompatActivity implements ICredentialsView {

    public static final String PROGRESS_DIALOG = "progress_dialog";
    @BindView(R.id.etLogin)
    EditText etLogin;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.tvSignUp1)
    TextView tvSignUp1;
    @BindView(R.id.tvSingUp2)
    TextView tvSignUp2;
    @BindView(R.id.tvSingUp3)
    TextView tvSignUp3;
    @BindView(R.id.tvSignUp4)
    TextView tvSignUp4;
    @BindView(R.id.tvOrSign)
    TextView tvSignIn;
    @Inject
    LoginPresenter loginPresenter;
    @Inject
    ActivityNavigator navigator;
    @Inject
    static Bus bus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);
        Log.e("actionBar", String.valueOf(getSupportActionBar()));
       // tvSignUp.setPaintFlags(tvSignUp.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        PeekabooApplication.getApp(this).getComponent().inject(this);
        loginPresenter.bind(this);
        loginPresenter.setCheckingInternet();
        bus.register(this);
    }

    @Override
    protected void onDestroy() {
        bus.unregister(this);
        loginPresenter.unbind();
        super.onDestroy();
    }

    @Override
    public void navigateToProfile() {
        navigator.startProfileActivity(this);
        finish();
    }

    @Override
    public void showInputError(InputFieldError error) {
        switch (error) {
            case LOGIN:
                etLogin.setError(getString(R.string.invalidLogin));
                etLogin.setText("");
                break;
            case PASSWORD:
                etPassword.setError(getString(R.string.invalidPassword));
                etPassword.setText("");
                break;
        }
    }

    @Override
    public void showProgress() {
        DialogFragment newFragment = ProgressDialogFragment.newInstance();
        newFragment.setCancelable(false);
        newFragment.show(getSupportFragmentManager(), PROGRESS_DIALOG);
    }

    @Override
    public void hideProgress() {
        DialogFragment fragment = (DialogFragment) getSupportFragmentManager().findFragmentByTag(PROGRESS_DIALOG);
        if (fragment != null) {
            fragment.dismiss();
        }
    }

    @OnClick(R.id.tvSingUp2)
    void ontvSingUpClick(){
        navigator.startSignUpActivity(this);
    }

    @OnClick(R.id.bSignIn)
    void onSignInButtonClick() {
        String login = etLogin.getText().toString();
        String password = etPassword.getText().toString();
        loginPresenter.onSignInButtonClick(login, password);
    }

    @OnClick(R.id.bVk)
    void onVkButtonClick(){
        loginPresenter.onVkButtonClick();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                Toast.makeText(getApplicationContext(),
                        "userID: " + res.userId + " email: " + res.email
                        , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(VKError error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onError(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
