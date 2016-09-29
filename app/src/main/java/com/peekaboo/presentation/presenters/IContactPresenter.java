package com.peekaboo.presentation.presenters;

import com.peekaboo.presentation.views.IContactsView;

/**
 * Created by Nikita on 11.08.2016.
 */
public interface IContactPresenter extends IPresenter<IContactsView> {

    void onCreate();
    void onDestroy();

    void loadContactsList();

}
