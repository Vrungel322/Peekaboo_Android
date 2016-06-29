package com.peekaboo.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.peekaboo.R;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.fragments.ProgressDialogFragment;
import com.peekaboo.presentation.presenters.LoginPresenter;
import com.peekaboo.presentation.views.ILoginView;
import com.peekaboo.utils.ActivityNavigator;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogInActivity extends AppCompatActivity implements ILoginView {

    public static final String PROGRESS_DIALOG = "progress_dialog";
    @BindView(R.id.etLogin)
    TextView etLogin;
    @BindView(R.id.etPassword)
    TextView etPassword;

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
        //запускает отлов состояния Интернета
        loginPresenter.setCheckingInternet();
//         get fingerprint for init in VK
//         need be added in VKConsole for each developer to test
//        loginPresenter.getFingerprint();
    }

    @Override
    protected void onDestroy() {
        loginPresenter.unbind();
        super.onDestroy();
    }

    @Override
    public void navigateToProfile() {
        navigator.startProfileActivity(this);
        finish();
    }

    @Override
    public void setCheckingInternet() {

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
                // Пользователь успешно авторизовался
                Toast.makeText(getApplicationContext(),
                        "userID: " + res.userId + " email: " + res.email
                        , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(VKError error) {
                // Произошла ошибка авторизации (например, пользователь запретил авторизацию)
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
