package com.peekaboo.presentation.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.glomadrian.codeinputlib.CodeInput;
import com.peekaboo.R;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.presenters.SignUpPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Nikita on 06.07.2016.
 */
public class ConfirmSignUpDialog extends DialogFragment {
    @BindView(R.id.ciConformation)
    CodeInput ciConformation;
    @Inject
    SignUpPresenter signUpPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PeekabooApplication.getApp(getActivity()).getComponent().inject(this);
        View view = inflater.inflate(R.layout.conformation_dialog_fragment, container, false);
        setCancelable(false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.bOk)
    public void bOkClicked(){
        StringBuilder pin = new StringBuilder();
        for (int i = 0; i < ciConformation.getCode().length; i++){
            pin.append(ciConformation.getCode()[i]);
        }
        signUpPresenter.onCodeConfirmButtonClick(String.valueOf(pin));
        dismiss();
    }
}
