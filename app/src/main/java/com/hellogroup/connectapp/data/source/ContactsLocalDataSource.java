package com.hellogroup.connectapp.data.source;

import com.hellogroup.connectapp.data.Contact;
import com.hellogroup.connectapp.util.AppExecutors;

import java.util.ArrayList;

import javax.inject.Inject;

public class ContactsLocalDataSource implements ContactsDataSource {

    private final AppExecutors mAppExecutors;

    private final ContactFetcher mContactFetcher;

    @Inject
    ContactsLocalDataSource(AppExecutors executors, ContactFetcher contactFetcher) {
        mAppExecutors = executors;
        mContactFetcher = contactFetcher;
    }

    @Override
    public void getContacts(final LoadContactsCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final ArrayList<Contact> contacts = mContactFetcher.getContacts();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (contacts.isEmpty()) {
                            callback.onContactsNotAvailable();
                        } else {
                            callback.onContactsLoaded(contacts);
                        }
                    }
                });
            }
        };

        //mAppExecutors.diskIO().execute(runnable);

        final ArrayList<Contact> contacts = mContactFetcher.getContacts();
        if (contacts.isEmpty()) {
            callback.onContactsNotAvailable();
        } else {
            callback.onContactsLoaded(contacts);
        }
    }

    @Override
    public void getContactDetails(long contactId, LoadContactDetailsCallback callback) {
        final Contact contact = mContactFetcher.getContactDetails(contactId);
        callback.onContactDetailsLoaded(contact);
    }

}
