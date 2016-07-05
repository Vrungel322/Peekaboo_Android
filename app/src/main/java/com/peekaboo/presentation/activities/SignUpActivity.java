package com.peekaboo.presentation.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.peekaboo.R;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.fragments.ProgressDialogFragment;
import com.peekaboo.presentation.presenters.LoginPresenter;
import com.peekaboo.presentation.presenters.SignUpPresenter;
import com.peekaboo.presentation.views.ICredentialsView;
import com.peekaboo.utils.ActivityNavigator;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sebastian on 05.07.16.
 */
public class SignUpActivity extends AppCompatActivity implements ICredentialsView {

    public static final String PROGRESS_DIALOG = "progress_dialog";
    @BindView(R.id.etLogin)
    EditText etLogin;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etPasswordConfirm)
    EditText etPasswordConfirm;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @Inject
    SignUpPresenter signUpPresenter;
    @Inject
    ActivityNavigator navigator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        PeekabooApplication.getApp(this).getComponent().inject(this);
        signUpPresenter.bind(this);
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

    @Override
    protected void onStart() {
        super.onStart();
        etPasswordConfirm.setText("asdasd");
        etPassword.setText("asdasd");
        etLogin.setText("asdasd");
        etEmail.setText("asdasd@mail.ru");
    }

    @Override
    protected void onDestroy() {
        signUpPresenter.unbind();
        super.onDestroy();
    }

    @Override
    public void navigateToProfile() {
        navigator.startProfileActivity(this);
        finish();
    }

    @Override
    public void onError(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.bSignIn)
    void onSignInButtonClick() {
        String login = etLogin.getText().toString();
        String password = etPassword.getText().toString();
        String passwordConfirm = etPasswordConfirm.getText().toString();
        String email = etEmail.getText().toString();
        signUpPresenter.onSignUpButtonClick(login, email, password, passwordConfirm);
    }
}
