package com.hellogroup.connectapp.addeditcontact;

import com.google.common.base.Strings;
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
    public void saveContact(Contact contact) {
        if(isNewContact()) {
            checkAndCreateContact(contact);
        } else {
            checkAndUpdateContact(contact);
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
            mAddEditContactView.setName(contact.getDisplayName(), contact.getPhoneNumber());
            mAddEditContactView.setPhone(contact.getPhoneNumber());
            mAddEditContactView.setEmail(contact.getEmailAddress());
        }
    }

    @Override
    public void onContactDetailsNotAvailable() {

    }

    private boolean checkContactDetails(Contact contact) {
        if(Strings.isNullOrEmpty(contact.getDisplayName())) {
            if(mAddEditContactView != null) mAddEditContactView.showEmptyNameError();
            return false;
        } else if(Strings.isNullOrEmpty(contact.getPhoneNumber())) {
            if(mAddEditContactView != null) mAddEditContactView.showPhoneError("Phone number cannot be empty!");
            return false;
        } else if(Strings.isNullOrEmpty(contact.getEmailAddress())) {
            if(mAddEditContactView != null) mAddEditContactView.showEmailError("Email address cannot be empty!");
            return false;
        } else if(contact.getPhoneNumber().length() != 10) {
            if(mAddEditContactView != null) mAddEditContactView.showPhoneError("Phone number must be of 10 digits!");
            return false;
        } else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(contact.getEmailAddress()).matches()) {
            if(mAddEditContactView != null) mAddEditContactView.showEmailError("Email should be of valid format!");
            return false;
        } else return true;
    }

    private void checkAndCreateContact(Contact contact) {
         if(checkContactDetails(contact)) {
             //contact.setContactId(mContactId);
             mContactsRepository.saveContact(contact);
             if(mAddEditContactView != null) {
                 mAddEditContactView.showContactList();
             }
         }
    }

    private void checkAndUpdateContact(Contact contact) {
        contact.setContactId(mContactId);
        if(checkContactDetails(contact)) {
            mContactsRepository.updateContact(contact);
            if(mAddEditContactView != null) {
                mAddEditContactView.showContactList();
            }
        }
    }
}
