package com.hellogroup.connectapp.data.source;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
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

    private void appendName(long contactId) {
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

    private void appendNumber(long contactId) {
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

    private void appendEmail(long contactId) {
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

    public void insertNewContact(Contact contact) {
        Uri addContactsUri = ContactsContract.Data.CONTENT_URI;
        long rawContactId = getRawContactId();
        insertContactDisplayName(addContactsUri, rawContactId, contact.getDisplayName());
        insertContactPhoneNumber(addContactsUri, rawContactId, contact.getPhoneNumber(), contact.getPhoneType());
        insertEmailContact(addContactsUri, rawContactId, contact.getEmailAddress(), contact.getEmailType());
    }

    // The purpose is to get a system generated raw contact id.
    private long getRawContactId() {
        ContentValues contentValues = new ContentValues();
        Uri rawContactUri = mContext.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, contentValues);
        return ContentUris.parseId(rawContactUri);
    }

    private void insertContactDisplayName(Uri addContactsUri, long rawContactId, String displayName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        contentValues.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, displayName);

        mContext.getContentResolver().insert(addContactsUri, contentValues);
    }

    private void insertContactPhoneNumber(Uri addContactsUri, long rawContactId, String phoneNumber, String phoneTypeStr) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        contentValues.put(Phone.NUMBER, phoneNumber);
        contentValues.put(Phone.TYPE, getPhoneType(phoneTypeStr));

        mContext.getContentResolver().insert(addContactsUri, contentValues);
    }

    private void insertEmailContact(Uri addContactsUri, long rawContactId, String emailAddress, String emailTypeStr) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
        contentValues.put(Email.ADDRESS, emailAddress);
        contentValues.put(Email.TYPE, getEmailType(emailTypeStr));

        mContext.getContentResolver().insert(addContactsUri, contentValues);
    }

    private int getPhoneType(String phoneTypeStr) {
        int phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME;
        switch (phoneTypeStr) {
            case "Home":
                phoneContactType = Phone.TYPE_HOME;
                break;
            case "Mobile":
                phoneContactType = Phone.TYPE_MOBILE;
                break;
            case "Work":
                phoneContactType = Phone.TYPE_WORK;
                break;
            case "Main":
                phoneContactType = Phone.TYPE_MAIN;
                break;
            case "Other":
                phoneContactType = Phone.TYPE_OTHER;
                break;
        }
        return phoneContactType;
    }

    private int getEmailType(String emailTypeStr) {
        int emailContactType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME;
        switch (emailTypeStr) {
            case "Custom":
                emailContactType =  Email.TYPE_CUSTOM;
                break;
            case "Home":
                emailContactType = Email.TYPE_HOME;
                break;
            case "Work":
                emailContactType = Email.TYPE_WORK;
                break;
            case "Mobile":
                emailContactType = Email.TYPE_OTHER;
                break;
            case "Other":
                emailContactType = Email.TYPE_MOBILE;
                break;
        }
        return emailContactType;
    }

    public void updateContactDetails(Contact contact) {
        ContentResolver contentResolver = mContext.getContentResolver();
        updateDisplayName(contentResolver, contact.getContactId(), contact.displayName);
        updatePhoneNumber(contentResolver, contact.getContactId(), getPhoneType(contact.getPhoneType()), contact.getPhoneNumber());
        updateEmailAddress(contentResolver, contact.getContactId(), getEmailType(contact.getEmailType()), contact.getEmailAddress());
    }

    /* Update phone number with raw contact id and phone type.*/
    private int updateDisplayName(ContentResolver contentResolver, long rawContactId, String displayName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, displayName);

        StringBuffer whereClauseBuf = new StringBuffer();

        // Specify the update contact id.
        whereClauseBuf.append(ContactsContract.Data.RAW_CONTACT_ID);
        whereClauseBuf.append("=");
        whereClauseBuf.append(rawContactId);

        // Specify the row data mimetype to phone mimetype( vnd.android.cursor.item/phone_v2 )
        whereClauseBuf.append(" and ");
        whereClauseBuf.append(ContactsContract.Data.MIMETYPE);
        whereClauseBuf.append(" = '");
        String mimetype = ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE;
        whereClauseBuf.append(mimetype);
        whereClauseBuf.append("'");

        // Update phone info through Data uri.Otherwise it may throw java.lang.UnsupportedOperationException.
        Uri dataUri = ContactsContract.Data.CONTENT_URI;

        // Get update data count.
        return contentResolver.update(dataUri, contentValues, whereClauseBuf.toString(), null);
    }

    /* Update phone number with raw contact id and phone type.*/
    private int updatePhoneNumber(ContentResolver contentResolver, long rawContactId, int phoneType, String newPhoneNumber) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Phone.NUMBER, newPhoneNumber);

        StringBuffer whereClauseBuf = new StringBuffer();

        // Specify the update contact id.
        whereClauseBuf.append(ContactsContract.Data.RAW_CONTACT_ID);
        whereClauseBuf.append("=");
        whereClauseBuf.append(rawContactId);

        // Specify the row data mimetype to phone mimetype( vnd.android.cursor.item/phone_v2 )
        whereClauseBuf.append(" and ");
        whereClauseBuf.append(ContactsContract.Data.MIMETYPE);
        whereClauseBuf.append(" = '");
        String mimetype = Phone.CONTENT_ITEM_TYPE;
        whereClauseBuf.append(mimetype);
        whereClauseBuf.append("'");

        // Specify phone type.
        whereClauseBuf.append(" and ");
        whereClauseBuf.append(Phone.TYPE);
        whereClauseBuf.append(" = ");
        whereClauseBuf.append(phoneType);

        // Update phone info through Data uri.Otherwise it may throw java.lang.UnsupportedOperationException.
        Uri dataUri = ContactsContract.Data.CONTENT_URI;

        // Get update data count.
        return contentResolver.update(dataUri, contentValues, whereClauseBuf.toString(), null);
    }

    private int updateEmailAddress(ContentResolver contentResolver, long rawContactId, int emailType, String newEmailAddress) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Email.ADDRESS, newEmailAddress);

        StringBuffer whereClauseBuf = new StringBuffer();

        // Specify the update contact id.
        whereClauseBuf.append(ContactsContract.Data.RAW_CONTACT_ID);
        whereClauseBuf.append("=");
        whereClauseBuf.append(rawContactId);

        // Specify the row data mimetype to email mimetype
        whereClauseBuf.append(" and ");
        whereClauseBuf.append(ContactsContract.Data.MIMETYPE);
        whereClauseBuf.append(" = '");
        String mimetype = Email.CONTENT_ITEM_TYPE;
        whereClauseBuf.append(mimetype);
        whereClauseBuf.append("'");

        // Specify email type.
        whereClauseBuf.append(" and ");
        whereClauseBuf.append(Email.TYPE);
        whereClauseBuf.append(" = ");
        whereClauseBuf.append(emailType);

        // Update phone info through Data uri.Otherwise it may throw java.lang.UnsupportedOperationException.
        Uri dataUri = ContactsContract.Data.CONTENT_URI;

        // Get update data count.
        return contentResolver.update(dataUri, contentValues, whereClauseBuf.toString(), null);
    }

}
