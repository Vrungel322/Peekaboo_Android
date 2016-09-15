package com.peekaboo.presentation.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peekaboo.R;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.presenters.ChatPresenter2;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sebastian on 10.09.16.
 */
public class RecordDialogFragment extends DialogFragment {

    public static final String TAG = "record_dialog";
    public static final String STATE = "state";
    public static final int RESET = 1;
    public static final int RECORDING = 2;
    public static final int RECORDED = 3;
    @BindView(R.id.fabRecord)
    FloatingActionButton fabRecord;
    @BindView(R.id.fabSend)
    FloatingActionButton fabSend;
    @Inject
    ChatPresenter2 presenter;

    public static RecordDialogFragment newInstance() {
        RecordDialogFragment fragment = new RecordDialogFragment();

        Bundle args = new Bundle();
        args.putInt(STATE, RESET);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PeekabooApplication.getApp(getActivity()).getComponent().inject(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_record, null);

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
        ButterKnife.bind(this, view);
        fabRecord.setOnClickListener(v -> presenter.onRecordButtonClick());
//        fabSend.setOnClickListener(v -> presenter.onRecordSend());
        switch (getArguments().getInt(STATE)) {
            case RESET:
                fabSend.setVisibility(View.GONE);
                fabRecord.setImageResource(R.drawable.ic_mic_white_36dp);
                break;
            case RECORDING:
                fabSend.setVisibility(View.GONE);
                fabRecord.setImageResource(R.drawable.ic_stop_white_36dp);
                break;
            case RECORDED:
                fabRecord.setVisibility(View.GONE);
                break;
        }
        return alertDialog;
    }

    public void showRecordStart() {
        fabRecord.setImageResource(R.drawable.ic_stop_white_36dp);
        getArguments().putInt(STATE, RECORDING);
    }

    public void showRecordStop() {
        fabRecord.setVisibility(View.GONE);
        fabSend.setVisibility(View.VISIBLE);
        getArguments().putInt(STATE, RECORDED);
    }
}
