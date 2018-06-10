package com.hellogroup.connectapp.addeditcontact;

import com.hellogroup.connectapp.di.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class AddEditContactModule {

    @ActivityScoped
    @Binds
    abstract AddEditContactContract.Presenter ContactPresenter(AddEditContactPresenter presenter);
}
