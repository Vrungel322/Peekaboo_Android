package com.peekaboo.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.peekaboo.R;

import butterknife.ButterKnife;

/**
 * Created by Nikita on 14.07.2016.
 */
public class ProfileFragment extends Fragment {
    private View rootView;

    public ProfileFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, rootView);
//        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Profile");

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}