package com.peekaboo.presentation.activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.klinker.android.sliding.SlidingActivity;
import com.peekaboo.R;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.app.view.PasswordView;
import com.peekaboo.presentation.dialogs.ConfirmSignUpDialog;
import com.peekaboo.presentation.dialogs.ProgressDialogFragment;
import com.peekaboo.presentation.views.ISignUpView;
import com.peekaboo.presentation.presenters.SignUpPresenter;
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
public class SignUpActivity extends SlidingActivity implements ISignUpView {

    public static final String PROGRESS_DIALOG = "progress_dialog";
    public static final String CONFIRM_SIGN_UP_DIALOG = "confirmSignUpDialog";
    @BindView(R.id.etLogin)
    EditText etLogin;
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etPasswordConfirm)
    EditText etPasswordConfirm;
//    @BindView(R.id.pvPassword)
//    PasswordView pvPassword;
//    @BindView(R.id.pvPasswordConfirm)
//    PasswordView pvPasswordConfirm;
    @BindView(R.id.ilUsername)
    TextInputLayout ilUsername;
    @BindView(R.id.ilPassword)
    TextInputLayout ilPassword;
    @BindView(R.id.ilLogin)
    TextInputLayout ilLogin;
    @BindView(R.id.ilPasswordConfirm)
    TextInputLayout ilPasswordCongirm;
//    @BindView(R.id.layoutSignUp)
//    RelativeLayout lSignUp;


    @Inject
    SignUpPresenter signUpPresenter;
    @Inject
    ActivityNavigator navigator;

    @Override
    public void init(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        //init(savedInstanceState);

//        setTitle("Activity Title");
//
//        setPrimaryColors(
//                getResources().getColor(R.color.colorPrimary),
//                getResources().getColor(R.color.colorPrimaryDark)
//        );



        setContent(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        PeekabooApplication.getApp(this).getComponent().inject(this);
        signUpPresenter.bind(this);
        enableFullscreen();
        disableHeader();
        //onSwipe();

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
        etUsername.setText("Vrungel");
        etLogin.setText("geronimoapachi@gmail.com");
//        pvPasswordConfirm.setText("asdasd");
//        pvPassword.setText("asdasd");
        etPasswordConfirm.setText("asdasd");
        etPassword.setText("asdasd");

    }

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
            case USERNAME:
                ilUsername.setErrorEnabled(true);
                ilUsername.setError(getString(R.string.invalidUsername));
                etUsername.setText("");
                break;
            case PASSWORD:
                ilPassword.setErrorEnabled(true);
                ilPassword.setError(getString(R.string.invalidPassword));
                etPassword.setText("");

//                pvPassword.setError(getString(R.string.invalidPassword));
//                pvPassword.setText("");
                break;
            case PASSWORD_CONFIRM:
                ilPassword.setErrorEnabled(true);
                ilPassword.setError(getString(R.string.invalidPasswordConfirm));
                ilPasswordCongirm.setErrorEnabled(true);
                ilPasswordCongirm.setError(getString(R.string.invalidPasswordConfirm));
                etPassword.setText("");
                etPasswordConfirm.setText("");

//                pvPassword.setError(getString(R.string.invalidPasswordConfirm));
//                pvPasswordConfirm.setError(getString(R.string.invalidPasswordConfirm));
//                pvPassword.setText("");
//                pvPasswordConfirm.setText("");
                break;
            case LOGIN:
                ilLogin.setErrorEnabled(true);
                ilLogin.setError(getString(R.string.invalidLogin));
                etLogin.setText("");
        }
    }

    @OnTouch(R.id.etUsername)
    boolean onUsernameEditText(){
        ilUsername.setErrorEnabled(false);
        ilUsername.setError("");
        return false;
    }

//
//    @OnTouch(R.id.pvPassword)
//    boolean onPasswordEditText(){
//        pvPassword.setError(null);
//        return false;
//    }
//    @OnTouch(R.id.pvPasswordConfirm)
//    boolean onPasswordConfirmEditText(){
//        pvPasswordConfirm.setError(null);
//        return false;
//    }


    @OnTouch(R.id.etPassword)
    boolean onPasswordEditText(){
        ilPassword.setErrorEnabled(false);
        ilPassword.setError("");
        return false;
    }
    @OnTouch(R.id.etPasswordConfirm)
    boolean onPasswordConfirmEditText(){
        ilPasswordCongirm.setErrorEnabled(false);
        ilPasswordCongirm.setError("");
        return false;
    }
    @OnTouch(R.id.etLogin)
    boolean onLoginEditText(){
        ilLogin.setErrorEnabled(false);
        ilLogin.setError("");
        return false;
    }



    @Override
    public void onError(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showConfirmDialog() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ConfirmSignUpDialog confirmSignUpDialog = new ConfirmSignUpDialog();
        confirmSignUpDialog.setStyle(android.app.DialogFragment.STYLE_NO_TITLE, 0);
        confirmSignUpDialog.setStyle(android.app.DialogFragment.STYLE_NO_FRAME, 0);
        confirmSignUpDialog.show(ft, CONFIRM_SIGN_UP_DIALOG);
    }

    @OnClick(R.id.bSignIn)
    void onSignInButtonClick() {
        String username = etUsername.getText().toString();
        String login = etLogin.getText().toString();
//        String password = pvPassword.getPassword();
//        String passwordConfirm = pvPasswordConfirm.getPassword();
        String password = etPassword.getText().toString();
        String passwordConfirm = etPasswordConfirm.getText().toString();
        signUpPresenter.onSignUpButtonClick(username, login, password, passwordConfirm);
    }

    private void onSwipe(){
//        lSignUp.setOnTouchListener(new OnSwipeTouchListener(SignUpActivity.this) {
//            public void onSwipeBottom() {
//                navigator.startLogInActivity(SignUpActivity.this);
//            }
//        });
    }
}
