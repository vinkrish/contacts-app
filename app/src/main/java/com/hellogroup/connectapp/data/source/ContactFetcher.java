package com.hellogroup.connectapp.data.source;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.content.CursorLoader;

import com.hellogroup.connectapp.data.Contact;

import java.util.ArrayList;

import javax.inject.Inject;

// new ContactFetcher(this).fetchAll();
public class ContactFetcher{

    private final Context mContext;

    @Inject
    ContactFetcher(Context context) {
        mContext = context;
    }

    private ArrayList<Contact> listContacts = new ArrayList<>();

    private Contact contact = new Contact();

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
                "display_name COLLATE NOCASE ASC" // the sort order (default)
        );

        Cursor c = cursorLoader.loadInBackground();

        if (c!=null && c.moveToFirst()) {
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

    public Contact getContactDetails(long contactId) {
        contact.contactId = contactId;
        appendName(contactId);
        appendNumber(contactId);
        appendEmail(contactId);
        return contact;
    }

    public void appendName(long contactId) {
        String[] projectionFields = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };

        String selection = ContactsContract.Contacts._ID + " = ?";

        String[] selectionArgs = { Long.toString(contactId) };

        CursorLoader cursorLoader = new CursorLoader(mContext,
                ContactsContract.Contacts.CONTENT_URI,
                projectionFields, // the columns to retrieve
                selection, // the selection criteria (none)
                selectionArgs, // the selection args (none)
                null // the sort order (default)
        );

        Cursor c = cursorLoader.loadInBackground();

        if (c.getCount()> 0 && c.moveToFirst()) {
            int nameIndex = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

            while (!c.isAfterLast()) {
                String contactDisplayName = c.getString(nameIndex);
                if(contactDisplayName != null) {
                    contact.setDisplayName(contactDisplayName);
                }
                c.moveToNext();
            }
        }
        c.close();
    }

    public void appendNumber(long contactId) {
        final String[] numberProjection = new String[]{
                Phone.NUMBER,
                Phone.TYPE,
                Phone.CONTACT_ID,
        };

        String selection = Phone.CONTACT_ID + " = ?";

        String[] selectionArgs = { Long.toString(contactId) };

        Cursor c = new CursorLoader(mContext,
                Phone.CONTENT_URI,
                numberProjection,
                selection,
                selectionArgs,
                null).loadInBackground();

        if (c.getCount()>0 && c.moveToFirst()) {
            int contactNumberColumnIndex = c.getColumnIndex(Phone.NUMBER);
            int contactTypeColumnIndex = c.getColumnIndex(Phone.TYPE);

            while (!c.isAfterLast()) {
                final String number = c.getString(contactNumberColumnIndex);
                final int type = c.getInt(contactTypeColumnIndex);
                contact.setPhoneNumber(number);

                String pType = "";
                switch (type) {
                    case Phone.TYPE_HOME:
                        pType = "Home";
                        break;
                    case Phone.TYPE_MOBILE:
                        pType = "Mobile";
                        break;
                    case Phone.TYPE_WORK:
                        pType = "Work";
                        break;
                    case Phone.TYPE_FAX_HOME:
                        pType = "Home Fax";
                        break;
                    case Phone.TYPE_FAX_WORK:
                        pType = "Work Fax";
                        break;
                    case Phone.TYPE_MAIN:
                        pType = "Main";
                        break;
                    case Phone.TYPE_OTHER:
                        pType = "Other";
                        break;
                }
                contact.setPhoneType(pType);
                c.moveToNext();
            }
        } else {
            contact.setPhoneNumber("");
            contact.setPhoneType("");
        }
        c.close();
    }

    public void appendEmail(long contactId) {
        final String[] emailProjection = new String[]{
                Email.ADDRESS,
                Email.TYPE,
                Email.CONTACT_ID,
        };

        String selection = Email.CONTACT_ID + " = ?";

        String[] selectionArgs = { Long.toString(contactId) };

        Cursor c = new CursorLoader(mContext,
                Email.CONTENT_URI,
                emailProjection,
                selection,
                selectionArgs,
                null).loadInBackground();

        if (c.getCount()>0 && c.moveToFirst()) {
            int emailAddressColumnIndex = c.getColumnIndex(Email.ADDRESS);
            int emailTypeColumnIndex = c.getColumnIndex(Email.TYPE);

            while (!c.isAfterLast()) {
                String address = c.getString(emailAddressColumnIndex);
                int type = c.getInt(emailTypeColumnIndex);
                contact.setEmailAddress(address);

                String eType = "";
                switch (type) {
                    case Email.TYPE_CUSTOM:
                        eType = "Custom";
                        break;
                    case Email.TYPE_HOME:
                        eType = "Home";
                        break;
                    case Email.TYPE_WORK:
                        eType = "Work";
                        break;
                    case Email.TYPE_OTHER:
                        eType = "Other";
                        break;
                    case Email.TYPE_MOBILE:
                        eType = "Mobile";
                        break;
                }
                contact.setEmailType(eType);
                c.moveToNext();
            }
        } else {
            contact.setEmailAddress("");
            contact.setEmailType("");
        }
        c.close();
    }

}
