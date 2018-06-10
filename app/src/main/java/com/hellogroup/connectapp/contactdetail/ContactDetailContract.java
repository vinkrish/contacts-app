package com.hellogroup.connectapp.contactdetail;

import com.hellogroup.connectapp.BasePresenter;
import com.hellogroup.connectapp.BaseView;

public interface ContactDetailContract {

    interface View extends BaseView<Presenter>{

        void setLoadingIndicator(boolean active);
    }

    interface Presenter extends BasePresenter<View>{

        void takeView(ContactDetailContract.View view);

        void dropView();
    }
}
