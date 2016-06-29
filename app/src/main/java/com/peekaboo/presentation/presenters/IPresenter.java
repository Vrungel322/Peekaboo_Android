package com.peekaboo.presentation.presenters;

import com.peekaboo.presentation.views.IView;

/**
 * Created by sebastian on 28.06.16.
 */
public interface IPresenter<V extends IView> {
    void bind(V view);
    void unbind();
}
