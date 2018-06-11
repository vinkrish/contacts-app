package com.hellogroup.connectapp.addeditcontact;

import android.support.annotation.Nullable;

import com.hellogroup.connectapp.di.ActivityScoped;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class AddEditContactModule {

    @Provides
    @ActivityScoped
    @Nullable
    static String provideContactId(AddEditContactActivity activity) {
        return activity.getIntent().getStringExtra(AddEditContactActivity.ARGUMENT_EDIT_CONTACT_ID);
    }

    @ActivityScoped
    @Binds
    abstract AddEditContactContract.Presenter ContactPresenter(AddEditContactPresenter presenter);
}
