package com.hellogroup.connectapp;

import android.support.annotation.VisibleForTesting;

import com.hellogroup.connectapp.contacts.ContactFetcher;
import com.hellogroup.connectapp.di.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class ContactApplication extends DaggerApplication {

    @Inject
    ContactFetcher contactFetcher;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }

    @VisibleForTesting
    public ContactFetcher getContactFetcher() {
        return contactFetcher;
    }
}
