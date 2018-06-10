package com.hellogroup.connectapp.data;

import android.net.Uri;

public class Contact {
    public long contactId;
    public Uri contactUri;
    public String displayName;

    public Contact(){};

    public Contact(long contactId, String displayName) {
        this.contactId = contactId;
        this.displayName = displayName;
    }
}
