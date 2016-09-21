package com.peekaboo.presentation.fragments;

import android.graphics.Color;
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
import android.widget.ListView;
import android.widget.Toast;

import com.peekaboo.R;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.adapters.ContactsListAdapter;
import com.peekaboo.presentation.presenters.ContactPresenter;
import com.peekaboo.presentation.views.IContactsView;


import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nikita on 13.07.2016.
 */
public class ContactsFragment extends Fragment implements IContactsView {

    @Inject
    ContactPresenter contactPresenter;
    @BindView(R.id.listViewIndexable)
    ListView listViewIndexable;
    private ContactsListAdapter contactsListAdapter;

    public ContactsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        PeekabooApplication.getApp(getActivity()).getComponent().inject(this);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_contacts));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        ButterKnife.bind(this, rootView);

        contactsListAdapter = new ContactsListAdapter(getActivity());
        listViewIndexable.setAdapter(contactsListAdapter);
        listViewIndexable.setFastScrollEnabled(true);
//        listViewIndexable.setOnItemClickListener((arg0, arg1, arg2, arg3) -> contactsListAdapter.onItemSelected(arg2));

        contactPresenter.bind(this);
        return rootView;
    }

    @Override
    public void showProgress() {
        showToastMessage("progress Started");
    }

    @Override
    public void hideProgress() {
        showToastMessage("progress Hide");
    }

    @Override
    public void showToastMessage(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showContactsList() {
        listViewIndexable.setBackgroundColor(Color.CYAN);
        showToastMessage("MAKE NOTICE");
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