package com.hellogroup.connectapp;

import android.support.annotation.VisibleForTesting;

import com.hellogroup.connectapp.data.source.ContactsRepository;
import com.hellogroup.connectapp.di.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class ContactApplication extends DaggerApplication {

    @Inject
    ContactsRepository contactsRepository;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }

    @VisibleForTesting
    public ContactsRepository getContactsRepository() {
        return contactsRepository;
    }
}
