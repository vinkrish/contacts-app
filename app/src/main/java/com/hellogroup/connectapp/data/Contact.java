package com.hellogroup.connectapp.data;

import android.net.Uri;

public class Contact {
    public long contactId;
    private String rawPhoneNumber;
    public Uri contactUri;
    public String displayName;
    public String phoneNumber;
    public String phoneType;
    public String emailAddress;
    public String emailType;

    public Contact(){};

    public Contact(long contactId, String displayName) {
        this.contactId = contactId;
        this.displayName = displayName;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public String getRawPhoneNumber() {
        return rawPhoneNumber;
    }

    public void setRawPhoneNumber(String rawPhoneNumber) {
        this.rawPhoneNumber = rawPhoneNumber;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Uri getContactUri() {
        return contactUri;
    }

    public void setContactUri(Uri contactUri) {
        this.contactUri = contactUri;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getEmailType() {
        return emailType;
    }

    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }
}
