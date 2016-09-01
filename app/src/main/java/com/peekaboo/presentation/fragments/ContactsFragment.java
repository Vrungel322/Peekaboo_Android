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
import com.peekaboo.data.repositories.database.contacts.PContact;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.presenters.ContactPresenter;

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
    ContactPresenter contactPresenter;

    @Inject
    public ContactsFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PeekabooApplication.getApp(getActivity()).getComponent().inject(this);
        contactPresenter.createTable("contactsTable");
        // Testing DB
        contactPresenter.insertContactToTable("contactsTable",
                new PContact("Name0", "Surname0", "Nickname0", true, "uri0", "a"));
        contactPresenter.insertContactToTable("contactsTable",
                new PContact("Name1", "Surname1", "Nickname1", true, "uri1", "b"));
        contactPresenter.insertContactToTable("contactsTable",
                new PContact("Name2", "Surname2", "Nickname2", true, "uri2", "c"));
        contactPresenter.getAllTableAsString("contactsTable");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Contacts");

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.contacts_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}