package com.peekaboo.presentation.presenters;

import com.peekaboo.domain.Dialog;
import com.peekaboo.presentation.views.IView;

import java.util.List;

/**
 * Created by Nikita on 28.09.2016.
 */
public interface IMainPresenter <V extends IView> extends IPresenter<V> {
    void fillHotAdapter();
}
