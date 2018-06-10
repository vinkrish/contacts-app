package com.hellogroup.connectapp.data;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract.Contacts;

public interface ContactsQuery {

    // An identifier for the loader
    final static int QUERY_ID = 1;

    // A content URI for the Contacts table
    final static Uri CONTENT_URI = Contacts.CONTENT_URI;

    // The search/filter query Uri
    final static Uri FILTER_URI = Contacts.CONTENT_FILTER_URI;

    // The selection clause for the CursorLoader query. The search criteria defined here
    // restrict results to contacts that have a display name and are linked to visible groups.
    // Notice that the search on the string provided by the user is implemented by appending
    // the search string to CONTENT_FILTER_URI.
    @SuppressLint("InlinedApi")
    final static String SELECTION = Contacts.DISPLAY_NAME_PRIMARY;

    // The desired sort order for the returned Cursor. In Android 3.0 and later, the primary
    // sort key allows for localization. In earlier versions. use the display name as the sort
    // key.
    @SuppressLint("InlinedApi")
    final static String SORT_ORDER = Contacts.SORT_KEY_PRIMARY;

    // The projection for the CursorLoader query. This is a list of columns that the Contacts
    // Provider should return in the Cursor.
    @SuppressLint("InlinedApi")
    final static String[] PROJECTION = {

            // The contact's row id
            Contacts._ID,

            Contacts.LOOKUP_KEY,

            Contacts.DISPLAY_NAME_PRIMARY,

            Contacts.PHOTO_THUMBNAIL_URI,

            SORT_ORDER,
    };

    // The query column numbers which map to each value in the projection
    final static int ID = 0;
    final static int LOOKUP_KEY = 1;
    final static int DISPLAY_NAME = 2;
    final static int PHOTO_THUMBNAIL_DATA = 3;
    final static int SORT_KEY = 4;
}
