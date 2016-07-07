package com.peekaboo.presentation.presenters;

import android.support.annotation.Nullable;

import com.peekaboo.presentation.views.IView;

/**
 * Created by Nikita on 27.06.2016.
 */
public abstract class BasePresenter<V extends IView> implements IPresenter<V> {

    @Nullable
    private V view;

    @Override
    public void bind(V view) {
        this.view = view;
    }

    @Override
    public void unbind() {
        this.view = null;
    }

    @Nullable
    public V getView() {
        return view;
    }
}
