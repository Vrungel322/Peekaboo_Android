package com.peekaboo.presentation.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.peekaboo.R;
import com.peekaboo.presentation.PeekabooApplication;
import com.peekaboo.presentation.animation.AnimationForImagesHelper;
import com.peekaboo.utils.Constants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nikita on 24.10.2016.
 */
public class PreviewImageFragment extends DialogFragment {
    @BindView(R.id.ivImageToPreview)
    ImageView ivImageToPreview;

    @Inject
    Picasso mPicasso;

    private String filePathOfImgToPreview;

    public PreviewImageFragment() {
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        filePathOfImgToPreview = args.getString(Constants.FILEPATH_OF_IMAGE_TO_PREVIEW);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PeekabooApplication.getApp(getActivity()).getComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.preview_image_fragment, container, false);
        ButterKnife.bind(this, view);
        mPicasso.load(Uri.fromFile(new File(filePathOfImgToPreview)))
                .error(R.drawable.ic_alert_circle_outline)
                .noPlaceholder()
                .into(ivImageToPreview, new Callback() {
                    @Override
                    public void onSuccess() {
                        AnimationForImagesHelper.setPreviewImageInChatAnimation(ivImageToPreview, 650);
                    }
                    @Override
                    public void onError() {}
                });
        return view;
    }
}
