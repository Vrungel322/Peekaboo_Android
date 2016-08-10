package com.peekaboo.presentation.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.glomadrian.codeinputlib.CodeInput;
import com.peekaboo.R;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.presenters.SignUpPresenter;

import javax.inject.Inject;

/**
 * Created by Nikita on 06.07.2016.
 */
public class ConfirmSignUpDialog extends DialogFragment {
    CodeInput ciConformation;
    @Inject
    SignUpPresenter signUpPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("dialog", "onCreateView");
        PeekabooApplication.getApp(getActivity()).getComponent().inject(this);
        View view = inflater.inflate(R.layout.conformation_dialog_fragment, container, false);
        ciConformation = (CodeInput) view.findViewById(R.id.ciConformation);
        view.findViewById(R.id.bOk).setOnClickListener(v -> bOkClicked());
        setCancelable(false);
        return view;
    }

    public void bOkClicked() {
        StringBuilder pin = new StringBuilder();
        for (int i = 0; i < ciConformation.getCode().length; i++) {
            pin.append(ciConformation.getCode()[i]);
        }
        signUpPresenter.onCodeConfirmButtonClick(String.valueOf(pin));
    }
}
