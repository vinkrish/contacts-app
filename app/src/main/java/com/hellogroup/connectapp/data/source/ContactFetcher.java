package com.hellogroup.connectapp.data.source;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.content.CursorLoader;

import com.hellogroup.connectapp.data.Contact;

import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;

// new ContactFetcher(this).fetchAll();
public class ContactFetcher{

    private final Context mContext;

    @Inject
    ContactFetcher(Context context) {
        mContext = context;
    }

    private ArrayList<Contact> listContacts = new ArrayList<>();

    public ArrayList<Contact> getContacts() {

        String[] projectionFields = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };

        CursorLoader cursorLoader = new CursorLoader(mContext,
                ContactsContract.Contacts.CONTENT_URI,
                projectionFields, // the columns to retrieve
                null, // the selection criteria (none)
                null, // the selection args (none)
                "display_name ASC" // the sort order (default)
        );

        Cursor c = cursorLoader.loadInBackground();

        if (c.moveToFirst()) {
            int idIndex = c.getColumnIndex(ContactsContract.Contacts._ID);
            int nameIndex = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

            while (!c.isAfterLast()) {
                long contactId = c.getLong(idIndex);
                String contactDisplayName = c.getString(nameIndex);
                Contact contact = new Contact(contactId, contactDisplayName);
                if(contact.displayName != null) {
                    listContacts.add(contact);
                }
                c.moveToNext();
            }
        }
        c.close();

        return listContacts;
    }

    public void matchContactNumbers(Map<String, Contact> contactsMap) {
        // Get numbers
        final String[] numberProjection = new String[]{
                Phone.NUMBER,
                Phone.TYPE,
                Phone.CONTACT_ID,
        };

        Cursor phone = new CursorLoader(mContext,
                Phone.CONTENT_URI,
                numberProjection,
                null,
                null,
                null).loadInBackground();

        if (phone.moveToFirst()) {
            final int contactNumberColumnIndex = phone.getColumnIndex(Phone.NUMBER);
            final int contactTypeColumnIndex = phone.getColumnIndex(Phone.TYPE);
            final int contactIdColumnIndex = phone.getColumnIndex(Phone.CONTACT_ID);

            while (!phone.isAfterLast()) {
                final String number = phone.getString(contactNumberColumnIndex);
                final String contactId = phone.getString(contactIdColumnIndex);
                Contact contact = contactsMap.get(contactId);
                if (contact == null) {
                    continue;
                }
                final int type = phone.getInt(contactTypeColumnIndex);
                String customLabel = "Custom";
                CharSequence phoneType = Phone.getTypeLabel(mContext.getResources(), type, customLabel);
                //contact.addNumber(number, phoneType.toString());
                phone.moveToNext();
            }
        }

        phone.close();
    }

}
