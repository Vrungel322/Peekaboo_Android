package com.peekaboo.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.peekaboo.R;
import com.peekaboo.domain.Dialog;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.activities.MainActivity;
import com.peekaboo.presentation.adapters.DialogsLargeAdapter;
import com.peekaboo.presentation.presenters.DialogPresenter;
import com.peekaboo.presentation.views.IDialogsView;
import com.peekaboo.utils.ActivityNavigator;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nikita on 13.07.2016.
 */
public class DialogsFragment extends Fragment implements IDialogsView {

    @BindView(R.id.recycler_dialog)
    public RecyclerView recyclerView;

    @Inject
    DialogPresenter presenter;
    @Inject
    ActivityNavigator navigator;

    private DialogsLargeAdapter adapter;

    public DialogsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PeekabooApplication.getApp(getActivity()).getComponent().inject(this);
        presenter.bind(this);

        View rootView = inflater.inflate(R.layout.fragment_dialogs, container, false);
        ButterKnife.bind(this, rootView);
//        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_dialogs));

        adapter = new DialogsLargeAdapter((MainActivity) getActivity(), navigator);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        presenter.loadDialogList();

        return rootView;
    }

    @Override
    public void onPause() {
        presenter.onPause();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        presenter.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dialogs_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showDialogsList(List<Dialog> dialogs) {
        adapter.setItems(dialogs);
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