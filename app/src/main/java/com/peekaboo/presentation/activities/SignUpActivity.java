package com.peekaboo.presentation.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.peekaboo.R;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.app.view.PasswordView;
import com.peekaboo.presentation.dialogs.ConfirmSignUpDialog;
import com.peekaboo.presentation.dialogs.ProgressDialogFragment;
import com.peekaboo.presentation.presenters.SignUpPresenter;
import com.peekaboo.presentation.utils.CredentialUtils;
import com.peekaboo.presentation.views.ISignUpView;
import com.peekaboo.utils.ActivityNavigator;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * Created by sebastian on 05.07.16.
 */
public class SignUpActivity  extends AppCompatActivity implements ISignUpView {

    public static final String PROGRESS_DIALOG = "progress_dialog";
    public static final String CONFIRM_SIGN_UP_DIALOG = "confirmSignUpDialog";

    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etLogin)
    EditText etLogin;
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.pvPassword)
    PasswordView pvPassword;
    @BindView(R.id.pvPasswordConfirm)
    PasswordView pvPasswordConfirm;
    @BindView(R.id.ilPhone)
    TextInputLayout ilPhone;
    @BindView(R.id.ilUsername)
    TextInputLayout ilUsername;
    @BindView(R.id.ilLogin)
    TextInputLayout ilLogin;


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
        etPhone.setText(CredentialUtils.getPhoneNumber(getApplicationContext()));
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
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        etUsername.setText("Vrungel");
//        etLogin.setText("geronimoapachi@gmail.com");
//        pvPasswordConfirm.setText("asdasd");
//        pvPassword.setText("asdasd");
//
//    }

    @Override
    protected void onDestroy() {
        signUpPresenter.unbind();
        super.onDestroy();
    }

    @Override
    public void navigateToProfile() {
        navigator.startProfileActivity(this);
    }


    @Override
    public void showInputError(InputFieldError error) {
        switch (error) {
            case PHONE_NUMBER:
                ilPhone.setErrorEnabled(true);
                ilPhone.setError(getString(R.string.invalidPhoneNumber));
                break;
            case USERNAME:
                ilUsername.setErrorEnabled(true);
                ilUsername.setError(getString(R.string.invalidUsername));
                etUsername.setText("");
                break;
            case PASSWORD:
                pvPassword.setError(getString(R.string.invalidPassword));
                pvPassword.setText("");
                break;
            case PASSWORD_CONFIRM:
                pvPassword.setError(getString(R.string.invalidPasswordConfirm));
                pvPasswordConfirm.setError(getString(R.string.invalidPasswordConfirm));
                pvPassword.setText("");
                pvPasswordConfirm.setText("");

                break;
            case LOGIN:
                ilLogin.setErrorEnabled(true);
                ilLogin.setError(getString(R.string.invalidLogin));
                etLogin.setText("");
        }
    }

    @OnTouch(R.id.etUsername)
    boolean onUsernameEditText() {
        ilUsername.setErrorEnabled(false);
        ilUsername.setError(null);
        return false;
    }


    @OnTouch(R.id.pvPassword)
    boolean onPasswordEditText() {
        pvPassword.setError(null);
        return false;
    }

    @OnTouch(R.id.pvPasswordConfirm)
    boolean onPasswordConfirmEditText() {
        pvPasswordConfirm.setError(null);
        return false;
    }

    @OnTouch(R.id.etLogin)
    boolean onLoginEditText() {
        ilLogin.setErrorEnabled(false);
        ilLogin.setError(null);
        return false;
    }

    @Override
    public void showToastMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showConfirmDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ConfirmSignUpDialog confirmSignUpDialog = new ConfirmSignUpDialog();
        confirmSignUpDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
//        confirmSignUpDialog.setStyle(android.app.DialogFragment.STYLE_NO_FRAME, 0);
        confirmSignUpDialog.show(ft, CONFIRM_SIGN_UP_DIALOG);
    }

    @Override
    public void dismissConfirmDialog() {
        DialogFragment dialogFragment = (DialogFragment) getSupportFragmentManager().findFragmentByTag(CONFIRM_SIGN_UP_DIALOG);
        if (dialogFragment != null) {
            dialogFragment.dismiss();
        }
    }

    @OnClick(R.id.bSignIn)
    void onSignInButtonClick() {
        String phone = etPhone.getText().toString();
        String username = etUsername.getText().toString();
        String login = etLogin.getText().toString();
        String password = pvPassword.getPassword();
        String passwordConfirm = pvPasswordConfirm.getPassword();
        signUpPresenter.onSignUpButtonClick(username, login, password, passwordConfirm);
    }

}
