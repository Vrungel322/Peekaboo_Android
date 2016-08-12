package com.peekaboo.presentation.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peekaboo.R;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.app.view.NormalCodeInput;
import com.peekaboo.presentation.presenters.SignUpPresenter;

import javax.inject.Inject;

/**
 * Created by Nikita on 06.07.2016.
 */
public class ConfirmSignUpDialog extends DialogFragment {
    NormalCodeInput ciConfirmation;
    @Inject
    SignUpPresenter signUpPresenter;
    private View okButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("dialog", "onCreateView");
        PeekabooApplication.getApp(getActivity()).getComponent().inject(this);
        View view = inflater.inflate(R.layout.confirmation_dialog_fragment, container, false);
        ciConfirmation = (NormalCodeInput) view.findViewById(R.id.ciConfirmation);
        ciConfirmation.setOnCodeChangedListener(code -> okButton.setEnabled(code[code.length - 1] != null));
        okButton = view.findViewById(R.id.bOk);
        okButton.setOnClickListener(v -> bOkClicked());
//        setCancelable(false);
        return view;
    }

    public void bOkClicked() {
        StringBuilder pin = new StringBuilder();
        for (int i = 0; i < ciConfirmation.getCode().length; i++) {
            pin.append(ciConfirmation.getCode()[i]);
        }
        signUpPresenter.onCodeConfirmButtonClick(String.valueOf(pin));
    }
}
