package com.peekaboo.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peekaboo.R;
import com.peekaboo.domain.SmsDialog;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.activities.MainActivity;
import com.peekaboo.presentation.adapters.SmsDialogsAdapter;
import com.peekaboo.presentation.presenters.SmsDialogPresenter;
import com.peekaboo.presentation.views.ISmsDialogsView;
import com.peekaboo.utils.ActivityNavigator;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by st1ch on 19.10.2016.
 */

public class SmsDialogsFragment extends Fragment implements ISmsDialogsView {

    @BindView(R.id.sms_dialogs_recycler_view)
    RecyclerView rvDialogs;
    @Inject
    SmsDialogPresenter presenter;
    @Inject
    ActivityNavigator navigator;

    private SmsDialogsAdapter adapter;

    public SmsDialogsFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(getString(R.string.title_dialogs));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PeekabooApplication.getApp(getActivity()).getComponent().inject(this);
        presenter.bind(this);
        presenter.onCreate();

        View rootView = inflater.inflate(R.layout.fragment_sms_dialogs, container, false);
        ButterKnife.bind(this, rootView);
//        setHasOptionsMenu(true);

        adapter = new SmsDialogsAdapter((MainActivity) getActivity(), navigator);
        rvDialogs.setAdapter(adapter);
        rvDialogs.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        presenter.loadDialogsList();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        presenter.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void showDialogsList(List<SmsDialog> dialogs) {
        adapter.setItems(dialogs);
    }

    @Override
    public void updateList() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showToastMessage(String text) {

    }
}
