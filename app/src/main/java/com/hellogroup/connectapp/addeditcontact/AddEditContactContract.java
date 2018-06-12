package com.hellogroup.connectapp.addeditcontact;

import com.hellogroup.connectapp.BasePresenter;
import com.hellogroup.connectapp.BaseView;
import com.hellogroup.connectapp.data.Contact;

public interface AddEditContactContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void setName(String name);

        void setPhone(String phone);

        void setEmail(String email);

        void showEmptyNameError();

        void showPhoneError(String error);

        void showEmailError(String error);
    }

    interface Presenter extends BasePresenter<View> {

        void takeView(AddEditContactContract.View view);

        void saveContact(Contact contact);

        void populateContact();
    }
}
