package com.peekaboo.presentation.fragments;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.peekaboo.R;
import com.peekaboo.data.mappers.PContactMapper;
import com.peekaboo.data.repositories.database.contacts.PContact;
import com.peekaboo.data.repositories.database.contacts.PContactHelper;
import com.peekaboo.data.repositories.database.messages.PMessageHelper;
import com.peekaboo.domain.User;
import com.peekaboo.domain.subscribers.BaseUseCaseSubscriber;
import com.peekaboo.domain.usecase.FindFriendUseCase;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.utils.ActivityNavigator;

import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by sebastian on 24.08.16.
 */
public class FriendTestFragment extends Fragment {

    @Inject
    FindFriendUseCase findFriendUseCase;
    @Inject
    ActivityNavigator navigator;
    @Inject
    PMessageHelper messageHelper;
    @Inject
    PContactHelper contactHelper;
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
                contactHelper.getAllContacts().subscribe(new Action1<List<PContact>>() {
                    @Override
                    public void call(List<PContact> pContacts) {
                        String contactId = null;
                        final String friendName = receiver.getText().toString();

                        for (PContact pContact : pContacts) {
                            String s = pContact.contactName();
                            if (s.equals(friendName)) {
                                contactId = pContact.contactId();
                                break;
                            }
                        }

                        if (contactId == null) {
                            findFriendUseCase.setFriendName(friendName);
                            findFriendUseCase.execute(new BaseUseCaseSubscriber<User>() {
                                @Override
                                public void onNext(User user) {
                                    super.onNext(user);

                                    PContactMapper pContactMapper = new PContactMapper();
                                    String contactId = user.getId();
                                    ContentValues values = pContactMapper.transform(
                                            new PContact(friendName, "surname", "nickname", false, "", contactId)
                                    );
                                    contactHelper.insert(values);
                                    messageHelper.createTable(contactId);
                                    navigator.startChatActivity(getActivity(), contactId);
                                }
                            });
                        } else {
                            navigator.startChatActivity(getActivity(), contactId);
                        }
                    }
                });
            }
        });
        return view;
    }
}
