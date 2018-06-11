package com.hellogroup.connectapp.contactdetail;

import com.hellogroup.connectapp.di.ActivityScoped;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import static com.hellogroup.connectapp.contactdetail.ContactDetailActivity.EXTRA_CONTACT_ID;

@Module
public abstract class ContactDetailModule {

    @Provides
    @ActivityScoped
    static long provideContactId(ContactDetailActivity activity) {
        return activity.getIntent().getLongExtra(EXTRA_CONTACT_ID, 0);
    }

    @ActivityScoped
    @Binds
    abstract ContactDetailContract.Presenter detailPresenter(ContactDetailPresenter presenter);
}
