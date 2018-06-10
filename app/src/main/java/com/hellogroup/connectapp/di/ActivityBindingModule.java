package com.hellogroup.connectapp.di;

import com.hellogroup.connectapp.addeditcontact.AddEditContactActivity;
import com.hellogroup.connectapp.addeditcontact.AddEditContactModule;
import com.hellogroup.connectapp.contactdetail.ContactDetailActivity;
import com.hellogroup.connectapp.contactdetail.ContactDetailModule;
import com.hellogroup.connectapp.contacts.ContactsActivity;
import com.hellogroup.connectapp.contacts.ContactsModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = ContactsModule.class)
    abstract ContactsActivity contactsActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = ContactDetailModule.class)
    abstract ContactDetailActivity contactDetailActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = AddEditContactModule.class)
    abstract AddEditContactActivity addEditContactActivity();
}
