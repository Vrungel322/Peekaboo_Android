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
import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.adapters.ContactsListAdapter;
import com.peekaboo.presentation.presenters.ContactPresenter;
import com.peekaboo.presentation.views.IContactsView;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nikita on 13.07.2016.
 */
@Singleton
public class ContactsFragment extends Fragment implements IContactsView {

    private View rootView;

    @Inject
    ContactPresenter contactPresenter;

    @BindView(R.id.listViewIndexable)
    ListView listViewIndexable;
    private ArrayList<String> list;
    //    private IndexableListView listViewIndexable;
    private ContactsListAdapter contactsListAdapter;

    @Inject
    public ContactsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PeekabooApplication.getApp(getActivity()).getComponent().inject(this);
        // Testing DB

        contactPresenter.bind(this);
        contactPresenter.insertContactToTable(new Contact("Name0",
                "Surname0",
                "Nickname0",
                true,
                "uri0"));
        contactPresenter.insertContactToTable(new Contact("Name1",
                "Surname1",
                "Nickname1",
                true,
                "uri1"));
        contactPresenter.insertContactToTable(new Contact("Name2",
                "Surname2",
                "Nickname2",
                true,
                "uri2"));

        contactPresenter.getAllTableAsString("ContactsDb");

    }

    @Override
    public void onResume() {
        super.onResume();
        //Make Query to get all real contacts from server
        //after that need to redo ContactListAdapter to match ContactsPOJO and json
        //contactPresenter.loadContactsList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_contacts));

        //hardcode list
        initList();

        contactsListAdapter = new ContactsListAdapter(list, getActivity());
        listViewIndexable.setAdapter(contactsListAdapter);
        listViewIndexable.setFastScrollEnabled(true);
        listViewIndexable.setOnItemClickListener((arg0, arg1, arg2, arg3) -> contactsListAdapter.onItemSelected(arg2));

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
    public void loadContactsList() {
        listViewIndexable.setBackgroundColor(Color.CYAN);
        showToastMessage("MAKE NOTICE");
    }

    private void initList() {

        if (list == null)
            list = new ArrayList<>();

        String[] countries = getResources().getStringArray(R.array.countries_array);
        for (String country : countries) {
            list.add(country);
        }
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