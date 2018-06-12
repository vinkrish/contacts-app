package com.hellogroup.connectapp.data.source;

import com.hellogroup.connectapp.data.Contact;
import com.hellogroup.connectapp.di.Device;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ContactsRepository implements ContactsDataSource{

    private final ContactsDataSource mContactLocalDataSource;

    @Inject
    ContactsRepository(@Device ContactsDataSource contactLocalDataSource) {
        mContactLocalDataSource = contactLocalDataSource;
    }

    @Override
    public void getContacts(final LoadContactsCallback callback) {
        mContactLocalDataSource.getContacts(new LoadContactsCallback() {
            @Override
            public void onContactsLoaded(ArrayList<Contact> contactList) {
                callback.onContactsLoaded(contactList);
            }

            @Override
            public void onContactsNotAvailable() {
                // Maybe Contact Server or fetch from Cache
            }
        });
    }

    @Override
    public void getContactDetails(long contactId, final LoadContactDetailsCallback callback) {
        mContactLocalDataSource.getContactDetails(contactId, new LoadContactDetailsCallback() {
            @Override
            public void onContactDetailsLoaded(Contact contact) {
                callback.onContactDetailsLoaded(contact);
            }

            @Override
            public void onContactDetailsNotAvailable() {
                // Indicate missing contact details
            }
        });
    }

    @Override
    public void saveContact(Contact contact) {
        mContactLocalDataSource.saveContact(contact);
    }

    @Override
    public void updateContact(Contact contact) {
        mContactLocalDataSource.updateContact(contact);
    }
}
