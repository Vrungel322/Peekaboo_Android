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
 * Created by Nikita on 13.07.2016.
 */
@Singleton
public class ContactsFragment extends Fragment {
    private View rootView;

    @Inject
    public ContactsFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        ButterKnife.bind(this, rootView);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Contacts");

        return rootView;
    }
}
