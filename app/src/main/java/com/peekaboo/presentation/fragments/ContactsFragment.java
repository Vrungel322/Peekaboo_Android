package com.peekaboo.presentation.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.peekaboo.R;
import com.peekaboo.data.repositories.database.contacts.Contact;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.adapters.ContactLargeAdapter;
import com.peekaboo.presentation.presenters.ContactPresenter;
import com.peekaboo.presentation.views.IContactsView;
import com.peekaboo.presentation.widget.RecyclerViewFastScroller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nikita on 13.07.2016.
 */
public class ContactsFragment extends Fragment implements IContactsView {

    @Inject
    ContactPresenter contactPresenter;
    @BindView(R.id.recyclerview)
    public RecyclerView recyclerView;

    private ArrayList<String> contactList;

    int numberOfItems;

    public ContactsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        PeekabooApplication.getApp(getActivity()).getComponent().inject(this);
        // Testing DB
//        contactPresenter.insertContactToTable(new Contact("Name0",
//                "Surname0",
//                "Nickname0",
//                true,
//                "uri0"));
//        contactPresenter.insertContactToTable(new Contact("Name1",
//                "Surname1",
//                "Nickname1",
//                true,
//                "uri1"));
//        contactPresenter.insertContactToTable(new Contact("Name2",
//                "Surname2",
//                "Nickname2",
//                true,
//                "uri2"));

        contactPresenter.getAllTableAsString();

    }

    @Override
    public void onResume() {
        super.onResume();
        //Make Query to get all real contacts from server
        //after that need to redo ContactListAdapter to match ContactsPOJO and json
        contactPresenter.loadContactsList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        ButterKnife.bind(this, rootView);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "ADD", Toast.LENGTH_LONG).show();
            }
        });

        initList();

        final ContactLargeAdapter adapter = new ContactLargeAdapter(contactList);
        recyclerView.setAdapter(adapter);
        final RecyclerViewFastScroller fastScroller = (RecyclerViewFastScroller) rootView.findViewById(R.id.fastscroller);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public void onLayoutChildren(final RecyclerView.Recycler recycler, final RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                //TODO if the items are filtered, considered hiding the fast scroller here
                final int firstVisibleItemPosition = findFirstVisibleItemPosition();
                if (firstVisibleItemPosition != 0) {
                    // this avoids trying to handle un-needed calls
                    if (firstVisibleItemPosition == -1)
                        //not initialized, or no items shown, so hide fast-scroller
                        fastScroller.setVisibility(View.GONE);
                    return;
                }
                final int lastVisibleItemPosition = findLastVisibleItemPosition();
                int itemsShown = lastVisibleItemPosition - firstVisibleItemPosition + 1;
                //if all items are shown, hide the fast-scroller
                fastScroller.setVisibility(adapter.getItemCount() > itemsShown ? View.VISIBLE : View.GONE);
            }
        });
        fastScroller.setRecyclerView(recyclerView);
        fastScroller.setViewsToUse(R.layout.recycler_view_fast_scroller__fast_scroller, R.id.fastscroller_bubble, R.id.fastscroller_handle);

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
    public void showContactsList(List<Contact> response) {
        recyclerView.setBackgroundColor(Color.CYAN);
        showToastMessage(response.get(1).contactName().toString());
    }

    @Override
    public void onDestroyView() {
        contactPresenter.unbind();
        super.onDestroyView();
    }

    private void initList() {

        if (contactList == null)
            contactList = new ArrayList<>();

        String[] countries = getResources().getStringArray(R.array.countries_array);
        for (String country : countries) {
            contactList.add(country);
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