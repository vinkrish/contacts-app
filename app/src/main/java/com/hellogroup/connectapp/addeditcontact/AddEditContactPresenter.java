package com.hellogroup.connectapp.addeditcontact;

import com.hellogroup.connectapp.data.source.ContactsRepository;
import com.hellogroup.connectapp.di.ActivityScoped;

import javax.inject.Inject;

@ActivityScoped
public class AddEditContactPresenter implements AddEditContactContract.Presenter {

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
    }

    @Override
    public void dropView() {
        mAddEditContactView = null;
    }
}
