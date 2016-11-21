package com.peekaboo.presentation.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import com.peekaboo.presentation.utils.ViewUtils;
import com.peekaboo.presentation.pojo.PhoneContactPOJO;
import com.peekaboo.presentation.presenters.ContactPresenter;
import com.peekaboo.presentation.views.IContactsView;
import com.peekaboo.presentation.widget.RecyclerViewFastScroller;
import com.peekaboo.utils.ActivityNavigator;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nikita on 13.07.2016.
 */
public class ContactsFragment extends Fragment implements IContactsView, MenuItemCompat.OnActionExpandListener {

    public static final String LAYOUT_MANAGER_STATE = "layout_manager_state";
    @Inject
    ContactPresenter contactPresenter;
    @Inject
    ActivityNavigator navigator;
    @Inject
    Picasso picasso;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.fastscroller)
    RecyclerViewFastScroller fastScroller;
    private MenuItem searchMenuItem;
    private SearchView mSearchView;
    private ContactLargeAdapter contactLargeAdapter;

    public ContactsFragment() {
    }

    public static ContactsFragment newInstance() {
        Bundle args = new Bundle();

        ContactsFragment fragment = new ContactsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
        PeekabooApplication.getApp(getActivity()).getComponent().inject(this);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(R.string.contacts);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);

        ButterKnife.bind(this, rootView);

//        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
//        fab.setOnClickListener(v -> {
//            test google maps
//                String geoUriString = "geo:50.2716,30.3125?z=15";
//                Uri geoUri = Uri.parse(geoUriString);
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, geoUri);
//                startActivity(mapIntent);
//            Intent lolintent = new Intent(getActivity(), MapActivity.class);
//            startActivity(lolintent);
//        });
        setUpRecyclerView();

        contactPresenter.bind(this);
        contactPresenter.onCreate();
        return rootView;
    }

    private void setUpRecyclerView() {
        contactLargeAdapter = new ContactLargeAdapter((AppCompatActivity) getActivity(), navigator, picasso);
        recyclerView.setAdapter(contactLargeAdapter);
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
                fastScroller.setVisibility(contactLargeAdapter.getItemCount() > itemsShown ? View.VISIBLE : View.GONE);
            }
        });
        fastScroller.setRecyclerView(recyclerView);
        fastScroller.setViewsToUse(R.layout.recycler_view_fast_scroller__fast_scroller,
                R.id.fastscroller_bubble, R.id.fastscroller_handle);
    }

    @Override
    public void showContactsList(List<Contact> response) {
        contactLargeAdapter.setItems(response);
        contactLargeAdapter.savedList(response);
        Parcelable state = getArguments().getParcelable(LAYOUT_MANAGER_STATE);
        if (state != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(state);
        }
    }

    @Override
    public void showPhoneContactList(List<PhoneContactPOJO> response) {
    }

    @Override
    public void showProgress() {
//        showToastMessage("progress Started");
    }

    @Override
    public void hideProgress() {
//        showToastMessage("progress Hide");
    }

    @Override
    public void showToastMessage(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        Parcelable parcelable = recyclerView.getLayoutManager().onSaveInstanceState();
        getArguments().putParcelable(LAYOUT_MANAGER_STATE, parcelable);
        contactPresenter.onDestroy();
        contactPresenter.unbind();
        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.contacts_menu, menu);
        searchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchMenuItem.getActionView();
        ViewUtils.changeSearchViewTextColor(mSearchView);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.equals(searchMenuItem)) {
            SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
            if (null != searchManager) {
                mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            }
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Toast.makeText(getContext(), newText, Toast.LENGTH_SHORT).show();
                    contactLargeAdapter.filter(newText);
                    return true;
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
//        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        recyclerView.setLayoutParams(lp);
        super.onResume();

    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
//        YoYo.with(Techniques.FadeInLeft)
//                .duration(700)
//                .playOn(mSearchView);
        return false;
    }
}