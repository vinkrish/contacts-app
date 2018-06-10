package com.hellogroup.connectapp.contactdetail;

import com.hellogroup.connectapp.di.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ContactDetailModule {

    @ActivityScoped
    @Binds
    abstract ContactDetailContract.Presenter detailPresenter(ContactDetailPresenter presenter);
}
