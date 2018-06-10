package com.hellogroup.connectapp.contacts;

import com.hellogroup.connectapp.di.ActivityScoped;

import javax.inject.Inject;

@ActivityScoped
public class ContactsPresenter implements ContactsContract.Presenter {

    private final ContactFetcher mContactFetcher;

    private ContactsContract.View mContactsView;

    @Inject
    ContactsPresenter(ContactFetcher contactFetcher){
        mContactFetcher = contactFetcher;
    }

    @Override
    public void takeView(ContactsContract.View view) {
        this.mContactsView = view;
        loadContacts();
    }

    @Override
    public void loadContacts() {
        if(mContactsView != null) {
            mContactsView.showContacts(mContactFetcher.fetchAll());
        }
    }

    @Override
    public void dropView() {
        mContactsView = null;
    }
}
