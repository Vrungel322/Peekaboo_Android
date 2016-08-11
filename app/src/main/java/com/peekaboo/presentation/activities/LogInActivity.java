package com.peekaboo.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.peekaboo.R;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.app.view.PasswordView;
import com.peekaboo.presentation.dialogs.ProgressDialogFragment;
import com.peekaboo.presentation.presenters.LoginPresenter;
import com.peekaboo.presentation.views.ICredentialsView;
import com.peekaboo.utils.ActivityNavigator;
import com.peekaboo.utils.OnSwipeTouchListener;
import com.squareup.otto.Bus;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class LogInActivity extends AppCompatActivity implements ICredentialsView {

    public static final String PROGRESS_DIALOG = "progress_dialog";
    @Inject
    static Bus bus;
    @BindView(R.id.etLogin)
    EditText etLogin;
    @BindView(R.id.ilUsername)
    TextInputLayout ilUsername;
    @BindView(R.id.pvPassword)
    PasswordView passwordView;
    @BindView(R.id.lLogIn)
    RelativeLayout lLogin;
    @BindView(R.id.tvSignUp)
    TextView tvSignUp;

    @Inject
    LoginPresenter loginPresenter;
    @Inject
    ActivityNavigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);
        PeekabooApplication.getApp(this).getComponent().inject(this);
        loginPresenter.bind(this);
        loginPresenter.setCheckingInternet();
        bus.register(this);
        onSwipe();
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
    }

    @Override
    public void showInputError(InputFieldError error) {
        switch (error) {
            case LOGIN:
                ilUsername.setErrorEnabled(true);
                ilUsername.setError(getResources().getString(R.string.invalidUsername));
                etLogin.setText("");
            case PASSWORD:
                passwordView.setError(getString(R.string.invalidPassword));
                passwordView.setText("");
                break;
        }
    }

    @Override
    public void showProgress() {
        DialogFragment fragment = (DialogFragment) getSupportFragmentManager().findFragmentByTag(PROGRESS_DIALOG);
        if (fragment == null) {
            DialogFragment newFragment = ProgressDialogFragment.newInstance();
            newFragment.setCancelable(false);
            newFragment.show(getSupportFragmentManager(), PROGRESS_DIALOG);
        }
    }

    @Override
    public void hideProgress() {
        DialogFragment fragment = (DialogFragment) getSupportFragmentManager().findFragmentByTag(PROGRESS_DIALOG);
        if (fragment != null) {
            fragment.dismiss();
        }
    }

    @OnClick(R.id.tvSignUp)
    void onSignUpClick(){
        navigator.startSignUpActivity(this);
    }

    @OnClick(R.id.bSignIn)
    void onSignInButtonClick() {
        String login = etLogin.getText().toString();
        String password = passwordView.getPassword();
        loginPresenter.onSignInButtonClick(login, password);
    }

    @OnTouch(R.id.etLogin)
    boolean onUsernameEditText(){
        ilUsername.setErrorEnabled(false);
        ilUsername.setError(null);
        return false;
    }


    @OnTouch(R.id.pvPassword)
    boolean onPasswordEditText(){
        passwordView.setError(null);
        return false;
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
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    private void onSwipe(){
        lLogin.setOnTouchListener(new OnSwipeTouchListener(LogInActivity.this) {
            public void onSwipeTop() {
                navigator.startSignUpActivity(LogInActivity.this);
            }
        });
        tvSignUp.setOnTouchListener(new OnSwipeTouchListener(LogInActivity.this) {
            public void onSwipeTop() {
                navigator.startSignUpActivity(LogInActivity.this);
            }
        });
    }
}
