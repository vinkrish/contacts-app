package com.hellogroup.connectapp.contactdetail;

import com.hellogroup.connectapp.contacts.ContactsContract;
import com.hellogroup.connectapp.data.Contact;
import com.hellogroup.connectapp.data.source.ContactsDataSource;
import com.hellogroup.connectapp.data.source.ContactsRepository;
import com.hellogroup.connectapp.di.ActivityScoped;

import javax.inject.Inject;

@ActivityScoped
public class ContactDetailPresenter implements ContactDetailContract.Presenter {

    private final ContactsRepository mContactsRepository;

    private ContactDetailContract.View mContactDetailView;

    @Inject
    ContactDetailPresenter(ContactsRepository contactsRepository){
        mContactsRepository = contactsRepository;
    }

    @Override
    public void takeView(ContactDetailContract.View view) {
        mContactDetailView = view;
    }

    @Override
    public void loadContactDetails(long contactId) {
        if(mContactDetailView != null) {
            mContactsRepository.getContactDetails(contactId, new ContactsDataSource.LoadContactDetailsCallback() {
                @Override
                public void onContactDetailsLoaded(Contact contact) {
                    if(mContactDetailView != null) {
                        mContactDetailView.showContact(contact);
                    }
                }
            });
        }
    }

    @Override
    public void openCaller() {
        if(mContactDetailView != null) {
            mContactDetailView.showCaller();
        }
    }

    @Override
    public void openMessenger() {
        if(mContactDetailView != null) {
            mContactDetailView.showMessenger();
        }
    }

    @Override
    public void openEmailClient() {
        if(mContactDetailView != null) {
            mContactDetailView.showEmailClient();
        }
    }

    @Override
    public void triggerContactNumberCopy() {
        if(mContactDetailView != null) {
            mContactDetailView.copyContactNumber();
        }
    }

    @Override
    public void triggerContactEmailCopy() {
        if(mContactDetailView != null) {
            mContactDetailView.copyContactEmail();
        }
    }

    @Override
    public void dropView() {
        mContactDetailView = null;
    }
}
