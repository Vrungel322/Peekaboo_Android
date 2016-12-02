package com.peekaboo.presentation.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.peekaboo.R;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.dialogs.AvatarChangeDialog;
import com.peekaboo.presentation.presenters.SettingsFragmentPresenter;
import com.peekaboo.presentation.utils.ResourcesUtils;
import com.peekaboo.utils.Constants;
import com.peekaboo.utils.Utility;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.vk.sdk.VKUIHelper.getApplicationContext;

/**
 * Created by Nikita on 14.07.2016.
 */
public class SettingsFragment extends Fragment implements ISettingsView {
    @Inject
    AccountUser accountUser;
    @Inject
    Picasso mPicasso;
    @Inject
    SettingsFragmentPresenter settingsFragmentPresenter;

    @BindView(R.id.userAvatarInSettings)
    ImageView userAvatarInSettings;
    @BindView(R.id.bLogInToTwitter)
    Button bLogInToTwitter;
    @BindView(R.id.etPhonenumber)
    EditText etPhonenumber;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etSurname)
    EditText etSurname;
    @BindView(R.id.etCountry)
    EditText etCountry;
    @BindView(R.id.etCity)
    EditText etCity;
    @Nullable
    private IUpdateAvatarInDrawer iUpdateAvatarInDrawer;

    public interface IUpdateAvatarInDrawer{
        void updateAvatarInDrawer();
    }

    public SettingsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PeekabooApplication.getApp(getActivity()).getComponent().inject(this);
        this.iUpdateAvatarInDrawer = (IUpdateAvatarInDrawer) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_layout, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Settings");
        showAvatar();
        settingsFragmentPresenter.bind(this);
//        etPhonenumber.setText(CredentialUtils.getPhoneNumber(getApplicationContext()));
        etName.setText(accountUser.getFirstName());
        etSurname.setText(accountUser.getLastName());
        etPhonenumber.setText(accountUser.getPhone());
        etCountry.setText(accountUser.getCountry());
        etCountry.setText(accountUser.getCountry());
        etCity.setText(accountUser.getCity());
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        showAvatar();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.settings_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save_preferences:{
                settingsFragmentPresenter.updateAccountData(accountUser);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.flSettingsIconAvatar)
    public void changeAvatar() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        AvatarChangeDialog avatarChangeDialog = new AvatarChangeDialog();
        avatarChangeDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        avatarChangeDialog.setListenerToUpdateAvatar(new AvatarChangeDialog.IAvatarChangeListener() {
            @Override
            public void takePhoto() {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = Utility.createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (photoFile != null) {
                        Uri cameraImageUri = Utility.getImageContentUri(getActivity(), photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
                        startActivityForResult(takePictureIntent, Constants.REQUEST_CODES.REQUEST_CODE_CAMERA);
                    }
                }
            }

            @Override
            public void takeFromGallery() {
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                        Constants.REQUEST_CODES.REQUEST_CODE_GALERY);
            }
        });
//        confirmSignUpDialog.setStyle(android.app.DialogFragment.STYLE_NO_FRAME, 0);
        avatarChangeDialog.show(ft, "avatar_change_dialog");
    }

    @OnClick(R.id.bLogInToTwitter)
    public void bLogInToTwitterClicked(){

        TwitterAuthClient mTwitterAuthClient= new TwitterAuthClient();
        mTwitterAuthClient.authorize(getActivity(), new com.twitter.sdk.android.core.Callback<TwitterSession>() {

            @Override
            public void success(Result<TwitterSession> twitterSessionResult) {
                // Success
                String msg = "@" + twitterSessionResult.data.getUserName()
                        + " logged in! (#" + twitterSessionResult.data.getUserId() + ")";
                showToastMessage(msg);
            }

            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.REQUEST_CODES.REQUEST_CODE_CAMERA:
                if (resultCode == RESULT_OK) {
                    settingsFragmentPresenter.updateAvatarInSettings(data.getData());
                }
                break;
            case Constants.REQUEST_CODES.REQUEST_CODE_GALERY:
                if (resultCode == RESULT_OK && data != null) {
                    Uri imageUri = data.getData();
                    settingsFragmentPresenter.updateAvatarInSettings(imageUri);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void updateAvatarViewInSettings(String result) {
        showProgress();
        if (result.equals("Ok")) {
            showAvatar();
            if (iUpdateAvatarInDrawer != null) {
                iUpdateAvatarInDrawer.updateAvatarInDrawer();
            }
        } else {
            showToastMessage("Error in updating avatar... Sorryan");
        }
    }

    @Override
    public void showToastMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();

    }

    private void showAvatar() {
        Picasso.with(getContext()).load(accountUser.getAvatar()).memoryPolicy(MemoryPolicy.NO_CACHE)
                .resize(0, ResourcesUtils.getDimenInPx(getContext(), R.dimen.widthOfIconInDrawer))
                .into(userAvatarInSettings);
    }

    @Override
    public void saveSettingsData(){
        accountUser.saveFirstName(etName.getText().toString());
        accountUser.saveLastName(etSurname.getText().toString());
        accountUser.savePhone(etPhonenumber.getText().toString());
        accountUser.saveCountry(etCountry.getText().toString());
        accountUser.saveCity(etCity.getText().toString());
    }

    @Override
    public void showProgress() {
//        showToastMessage("showProgress");
    }

    @Override
    public void hideProgress() {
//        showToastMessage("hideProgress");
    }

    @Override
    public void onDestroyView() {
        settingsFragmentPresenter.unbind();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        this.iUpdateAvatarInDrawer = null;
        super.onDestroy();
    }
}