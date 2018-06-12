package com.hellogroup.connectapp.data.source;

import com.hellogroup.connectapp.data.Contact;

import java.util.ArrayList;

public interface ContactsDataSource {

    interface LoadContactsCallback{

        void onContactsLoaded(ArrayList<Contact> contactList);

        void onContactsNotAvailable();
    }

    interface LoadContactDetailsCallback {

        void onContactDetailsLoaded(Contact contact);

        void onContactDetailsNotAvailable();
    }

    interface SaveContactCallback {

        void onContactSaved();

        void onFailedToSaveContact();
    }

    void getContacts(LoadContactsCallback callback);

    void getContactDetails(long contactId, LoadContactDetailsCallback callback);

    void saveContact(Contact contact);

    void updateContact(Contact contact);

}
