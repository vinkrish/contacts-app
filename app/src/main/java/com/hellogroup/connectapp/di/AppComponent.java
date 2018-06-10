package com.hellogroup.connectapp.di;

import android.app.Application;

import com.hellogroup.connectapp.ContactApplication;
import com.hellogroup.connectapp.data.source.ContactsRepository;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = { ContactsRepositoryModule.class,
        ApplicationModule.class,
        ActivityBindingModule.class,
        AndroidSupportInjectionModule.class})
public interface AppComponent extends AndroidInjector<ContactApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
