package com.hellogroup.connectapp.contactdetail;

import com.hellogroup.connectapp.BasePresenter;
import com.hellogroup.connectapp.BaseView;
import com.hellogroup.connectapp.data.Contact;

public interface ContactDetailContract {

    interface View extends BaseView<Presenter>{

        void setLoadingIndicator(boolean active);

        void showContact(Contact contact);

        void showMissingContact();

        void showCaller();

        void showMessenger();

        void showEmailClient();

        void copyContactNumber();

        void copyContactEmail();
    }

    interface Presenter extends BasePresenter<View>{

        void takeView(ContactDetailContract.View view);

        void loadContactDetails();

        void openCaller();

        void openMessenger();

        void openEmailClient();

        void triggerContactNumberCopy();

        void triggerContactEmailCopy();
    }
}
