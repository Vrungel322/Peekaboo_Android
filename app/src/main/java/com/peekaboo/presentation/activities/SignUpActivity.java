package com.peekaboo.presentation.activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.peekaboo.R;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.fragments.ConfirmSignUpDialog;
import com.peekaboo.presentation.fragments.ProgressDialogFragment;
import com.peekaboo.presentation.presenters.ISingUpView;
import com.peekaboo.presentation.presenters.SignUpPresenter;
import com.peekaboo.utils.ActivityNavigator;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sebastian on 05.07.16.
 */
public class SignUpActivity extends AppCompatActivity implements ISingUpView {

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
    @Inject
    ConfirmSignUpDialog confirmSignUpDialog;

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
            case PASSWORD_CONFIRM:
                etPassword.setError(getString(R.string.invalidPasswordConfirm));
                etPassword.setText("");
                etPasswordConfirm.setText("");
                break;
            case EMAIL:
                etEmail.setError(getString(R.string.invalidEmail));
                etEmail.setText("");
        }
    }

    @Override
    public void onError(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showConfirmDialog() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        confirmSignUpDialog.setStyle(android.app.DialogFragment.STYLE_NO_TITLE, 0);
        confirmSignUpDialog.setStyle(android.app.DialogFragment.STYLE_NO_FRAME, 0);
        confirmSignUpDialog.show(ft, "dfNewPerson");
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
