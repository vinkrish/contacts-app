package com.hellogroup.connectapp.addeditcontact;

import com.hellogroup.connectapp.BasePresenter;
import com.hellogroup.connectapp.BaseView;

public interface AddEditContact {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);
    }

    interface Presenter extends BasePresenter<View> {

        void takeView(AddEditContact.View view);

        void dropView();
    }
}
