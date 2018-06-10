package com.hellogroup.connectapp.contacts;

import com.hellogroup.connectapp.di.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ContactsModule {

    @ActivityScoped
    @Binds
    abstract ContactsContract.Presenter contactPresenter(ContactsPresenter presenter);
}
