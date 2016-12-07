package com.peekaboo.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peekaboo.R;
import com.peekaboo.presentation.app.view.OnlineIndicatorView;

/**
 * Created by arkadius on 9/28/16.
 */

public class BlankDialogFragment extends Fragment {

    private OnlineIndicatorView ind1;
    private OnlineIndicatorView ind2;
    private OnlineIndicatorView ind3;
    private OnlineIndicatorView ind4;
    private OnlineIndicatorView ind5;
    private OnlineIndicatorView ind6;
    private OnlineIndicatorView ind7;
    private OnlineIndicatorView ind8;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Dialogs");

        return inflater.inflate(R.layout.fragment_blank_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ind1 = (OnlineIndicatorView) view.findViewById(R.id.ind1);
        ind2 = (OnlineIndicatorView)view.findViewById(R.id.ind2);
        ind3 = (OnlineIndicatorView)view.findViewById(R.id.ind3);
        ind4 = (OnlineIndicatorView)view.findViewById(R.id.ind4);
        ind5 = (OnlineIndicatorView)view.findViewById(R.id.ind5);
        ind6 = (OnlineIndicatorView)view.findViewById(R.id.ind6);
        ind7 = (OnlineIndicatorView)view.findViewById(R.id.ind7);
        ind8 = (OnlineIndicatorView)view.findViewById(R.id.ind8);
        ind1.setState(true, 0);
        ind2.setState(false, 0);
        ind3.setState(true, 5);
        ind4.setState(false, 5);
        ind5.setState(true, 65);
        ind6.setState(false, 65);
        ind7.setState(true, 100);
        ind8.setState(false, 100);

    }
}
