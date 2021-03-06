package com.hellogroup.connectapp.contacts;

import android.app.Activity;
import android.util.Log;

import com.hellogroup.connectapp.addeditcontact.AddEditContactActivity;
import com.hellogroup.connectapp.data.Contact;
import com.hellogroup.connectapp.data.source.ContactsDataSource;
import com.hellogroup.connectapp.data.source.ContactsRepository;
import com.hellogroup.connectapp.di.ActivityScoped;

import java.util.ArrayList;

import javax.inject.Inject;

@ActivityScoped
public class ContactsPresenter implements ContactsContract.Presenter {

    private final ContactsRepository mContactsRepository;

    private ContactsContract.View mContactsView;

    @Inject
    ContactsPresenter(ContactsRepository contactsRepository){
        mContactsRepository = contactsRepository;
    }

    @Override
    public void result(int requestCode, int resultCode) {
        if (AddEditContactActivity.REQUEST_ADD_CONTACT == requestCode
                && Activity.RESULT_OK == resultCode) {
            if (mContactsView != null) {
                mContactsView.showSuccessfullySavedMessage();
            }
        }
    }

    @Override
    public void takeView(ContactsContract.View view) {
        this.mContactsView = view;
        loadContacts();
    }

    @Override
    public void loadContacts() {
        if(mContactsView != null) {
            mContactsView.setLoadingIndicator(true);
            mContactsRepository.getContacts(new ContactsDataSource.LoadContactsCallback() {
                @Override
                public void onContactsLoaded(ArrayList<Contact> contactList) {
                    if(mContactsView != null) {
                        mContactsView.showContacts(contactList);
                        mContactsView.setLoadingIndicator(false);
                    }
                }

                @Override
                public void onContactsNotAvailable() {
                    if(mContactsView != null) {
                        mContactsView.showNoContacts();
                        mContactsView.setLoadingIndicator(false);
                    }
                }
            });
        }
    }

    @Override
    public void openContactDetails(Contact requestedContact) {
        if(mContactsView != null) {
            mContactsView.showContactDetailsUi(requestedContact.contactId);
        }
    }

    @Override
    public void addNewContact() {
        if(mContactsView != null) {
            mContactsView.showAddContact();
        }
    }

    @Override
    public void dropView() {
        mContactsView = null;
    }
}
