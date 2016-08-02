package com.peekaboo.presentation.activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.peekaboo.R;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.fragments.ConfirmSignUpDialog;
import com.peekaboo.presentation.fragments.ProgressDialogFragment;
import com.peekaboo.presentation.views.ISignUpView;
import com.peekaboo.presentation.presenters.SignUpPresenter;
import com.peekaboo.utils.ActivityNavigator;
import com.peekaboo.utils.OnSwipeTouchListener;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * Created by sebastian on 05.07.16.
 */
public class SignUpActivity extends AppCompatActivity implements ISignUpView {

    public static final String PROGRESS_DIALOG = "progress_dialog";
    @BindView(R.id.etLogin)
    EditText etLogin;
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etPasswordConfirm)
    EditText etPasswordConfirm;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        PeekabooApplication.getApp(this).getComponent().inject(this);
        signUpPresenter.bind(this);
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
        etLogin.setText("nikita241296@gmail.com");
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
        finish();
    }


    @Override
    public void showInputError(InputFieldError error) {
        switch (error) {
            case USERNAME:
                ilUsername.setError(getString(R.string.invalidUsername));
                etUsername.setText("");
                break;
            case PASSWORD:
                ilPassword.setError(getString(R.string.invalidPassword));
                etPassword.setText("");
                break;
            case PASSWORD_CONFIRM:
                ilPassword.setError(getString(R.string.invalidPasswordConfirm));
                ilPasswordCongirm.setError(getString(R.string.invalidPasswordConfirm));
                etPassword.setText("");
                etPasswordConfirm.setText("");
                break;
            case LOGIN:
                ilLogin.setError(getString(R.string.invalidLogin));
                etLogin.setText("");
        }
    }

    @OnTouch(R.id.etUsername)
    boolean onUsernameEditText(){
        ilUsername.setError(null);
        return false;
    }


    @OnTouch(R.id.etPassword)
    boolean onPasswordEditText(){
        ilPassword.setError(null);
        return false;
    }
    @OnTouch(R.id.etPasswordConfirm)
    boolean onPasswordConfirmEditText(){
        ilPasswordCongirm.setError(null);
        return false;
    }
    @OnTouch(R.id.etLogin)
    boolean onLoginEditText(){
        ilLogin.setError(null);
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
        confirmSignUpDialog.show(ft, "confirmSignUpDialog");
    }

    @OnClick(R.id.bSignIn)
    void onSignInButtonClick() {
        String username = etUsername.getText().toString();
        String login = etLogin.getText().toString();
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
