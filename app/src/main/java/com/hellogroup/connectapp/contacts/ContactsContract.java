package com.hellogroup.connectapp.contacts;

import com.hellogroup.connectapp.BasePresenter;
import com.hellogroup.connectapp.BaseView;
import com.hellogroup.connectapp.data.Contact;

import java.util.ArrayList;

public interface ContactsContract {

    interface View extends BaseView<Presenter>{

        void setLoadingIndicator(boolean active);

        void showContacts(ArrayList<Contact> contactList);
    }

    interface Presenter extends BasePresenter<View>{

        void takeView(ContactsContract.View view);

        void loadContacts();

        void dropView();
    }
}
