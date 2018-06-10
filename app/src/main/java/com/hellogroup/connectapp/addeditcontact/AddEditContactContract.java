package com.hellogroup.connectapp.addeditcontact;

import com.hellogroup.connectapp.BasePresenter;
import com.hellogroup.connectapp.BaseView;

public interface AddEditContactContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);
    }

    interface Presenter extends BasePresenter<View> {

        void takeView(AddEditContactContract.View view);

        void dropView();
    }
}
