package com.peekaboo.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peekaboo.R;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.activities.MainActivity;
import com.peekaboo.presentation.adapters.TabsAdapter;
import com.peekaboo.utils.ActivityNavigator;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nataliia on 28.10.2016.
 */

public class DialogsFragment extends Fragment {
    @Inject
    ActivityNavigator navigator;
    TabLayout tabLayout;
    @BindView(R.id.pager)
    ViewPager viewPager;

    private TabsAdapter adapter;

    public static DialogsFragment newInstance() {
        Bundle args = new Bundle();
        DialogsFragment fragment = new DialogsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setTitle(R.string.title_dialogs);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = ((MainActivity) getActivity()).getTabLayout();
        tabLayout.setVisibility(View.VISIBLE);
        adapter = new TabsAdapter(getChildFragmentManager(), getActivity());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
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
    public void onDestroyView() {
        tabLayout.setVisibility(View.GONE);
        tabLayout.setupWithViewPager(null);
        tabLayout = null;
        super.onDestroyView();
    }
}
