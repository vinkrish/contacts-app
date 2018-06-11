package com.hellogroup.connectapp.addeditcontact;

import com.hellogroup.connectapp.data.Contact;
import com.hellogroup.connectapp.data.source.ContactsDataSource;
import com.hellogroup.connectapp.data.source.ContactsRepository;
import com.hellogroup.connectapp.di.ActivityScoped;

import javax.inject.Inject;

@ActivityScoped
public class AddEditContactPresenter implements AddEditContactContract.Presenter,
        ContactsDataSource.LoadContactDetailsCallback {

    private final ContactsRepository mContactsRepository;

    private AddEditContactContract.View mAddEditContactView;

    private long mContactId;

    @Inject
    AddEditContactPresenter(long contactId, ContactsRepository contactsRepository){
        mContactId = contactId;
        mContactsRepository = contactsRepository;
    }

    @Override
    public void takeView(AddEditContactContract.View view) {
        this.mAddEditContactView = view;
        if(!isNewContact()) {
            populateContact();
        }
    }

    @Override
    public void populateContact() {
        if(isNewContact()) {
            throw new RuntimeException("New Contact but populateContact is called!");
        }
        mContactsRepository.getContactDetails(mContactId, this);
    }

    private boolean isNewContact() {
        return mContactId == 0;
    }

    @Override
    public void dropView() {
        mAddEditContactView = null;
    }

    @Override
    public void onContactDetailsLoaded(Contact contact) {
        if(mAddEditContactView != null) {
            mAddEditContactView.setName(contact.getDisplayName());
            mAddEditContactView.setPhone(contact.getPhoneNumber());
            mAddEditContactView.setEmail(contact.getEmailAddress());
        }
    }

    @Override
    public void onContactDetailsNotAvailable() {

    }
}
