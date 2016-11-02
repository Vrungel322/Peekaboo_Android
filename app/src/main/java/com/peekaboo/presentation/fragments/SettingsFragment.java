package com.peekaboo.presentation.fragments;

import android.os.Bundle;
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

import com.peekaboo.R;
import com.peekaboo.presentation.dialogs.AvatarChangeDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Nikita on 14.07.2016.
 */
public class SettingsFragment extends Fragment {
    private View rootView;

    public SettingsFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.settings_layout, container, false);
        ButterKnife.bind(this, rootView);
//        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Settings");

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.settings_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.settings_icon)
    public void changeAvatar(){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        AvatarChangeDialog avatarChangeDialog = new AvatarChangeDialog();
        avatarChangeDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
//        confirmSignUpDialog.setStyle(android.app.DialogFragment.STYLE_NO_FRAME, 0);
        avatarChangeDialog.show(ft, "avatar_change_dialog");
    }
}