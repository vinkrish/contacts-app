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
    static long provideContactId(AddEditContactActivity activity) {
        return activity.getIntent().getLongExtra(AddEditContactActivity.ARGUMENT_EDIT_CONTACT_ID, 0);
    }

    @ActivityScoped
    @Binds
    abstract AddEditContactContract.Presenter ContactPresenter(AddEditContactPresenter presenter);
}
