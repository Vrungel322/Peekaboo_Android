package com.peekaboo.presentation.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peekaboo.R;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.ButterKnife;

/**
 * Created by Nikita on 14.07.2016.
 */
public class SettingsFragment extends Fragment {
    private View rootView;

    public SettingsFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, rootView);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Settings");

        return rootView;
    }
}