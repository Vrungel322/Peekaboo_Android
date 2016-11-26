package com.peekaboo.presentation.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TabHost;

import com.peekaboo.R;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.activities.MainActivity;
import com.peekaboo.presentation.adapters.DialogsLargeAdapter;
import com.peekaboo.presentation.adapters.TabsAdapter;
import com.peekaboo.utils.ActivityNavigator;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nataliia on 28.10.2016.
 */

public class DialogsFragment extends Fragment implements TabLayout.OnTabSelectedListener {
    @Inject
    ActivityNavigator navigator;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.pager)
    ViewPager viewPager;

    private TabsAdapter adapter;
    private String[] tabNames;
    private View.OnLayoutChangeListener listener1 = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            int count = getChildFragmentManager().getBackStackEntryCount();
            Log.e("backstack", "count = " + count);
        }
    };

    public static DialogsFragment newInstance() {

        Bundle args = new Bundle();

        DialogsFragment fragment = new DialogsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private MainActivity getMainActivity() {
        return ((MainActivity) getActivity());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity activity = getMainActivity();
        activity.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.burger);
        activity.getSupportActionBar().setTitle(R.string.title_dialogs);
        tabNames = getResources().getStringArray(R.array.dialog_tab_names);

        adapter = new TabsAdapter(getChildFragmentManager(), tabNames);


        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getMainActivity().onBurgerPress();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PeekabooApplication.getApp(getActivity()).getComponent().inject(this);


        View rootView = inflater.inflate(R.layout.tab_dialogs, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
