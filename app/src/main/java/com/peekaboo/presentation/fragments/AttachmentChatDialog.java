package com.peekaboo.presentation.fragments;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.peekaboo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nataliia on 12.07.2016.
 */
public class AttachmentChatDialog  extends DialogFragment {
    @BindView(R.id.attach_list)
    ListView lvAttachments;

    private String[] attach_list_strings;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        attach_list_strings = getResources().getStringArray(R.array.attacment_list);

        View view = inflater.inflate(R.layout.attach_dialog, container, false);
        ButterKnife.bind(this, view);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), R.layout.attach_dialog_item, attach_list_strings);

        lvAttachments.setAdapter(adapter);
        lvAttachments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case 0:
                        Toast.makeText(getActivity(), "0", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getActivity(), "1", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getActivity(), "2", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(getActivity(), "3", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        return view;
    }
}
