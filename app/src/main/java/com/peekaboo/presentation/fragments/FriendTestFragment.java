package com.peekaboo.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.peekaboo.R;
import com.peekaboo.domain.User;
import com.peekaboo.domain.subscribers.BaseUseCaseSubscriber;
import com.peekaboo.domain.usecase.FindFriendUseCase;
import com.peekaboo.presentation.PeekabooApplication;

import javax.inject.Inject;

/**
 * Created by sebastian on 24.08.16.
 */
public class FriendTestFragment extends Fragment {

    @Inject
    FindFriendUseCase findFriendUseCase;
    private EditText receiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PeekabooApplication.getApp(getActivity()).getComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_friend_test, container, false);
        receiver = (EditText) view.findViewById(R.id.receiverView);
        view.findViewById(R.id.findButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findFriendUseCase.setFriendName(receiver.getText().toString());
                findFriendUseCase.execute(new BaseUseCaseSubscriber<User>() {
                    @Override
                    public void onNext(User user) {
                        super.onNext(user);
                        Fragment newFragment = MessangerTestFragment.newInstance(user.getId());
                        getFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainer, newFragment)
                                .commit();
                    }
                });
            }
        });
        return view;
    }
}
