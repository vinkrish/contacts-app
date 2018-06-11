package com.hellogroup.connectapp.di;

import com.hellogroup.connectapp.data.source.ContactsDataSource;
import com.hellogroup.connectapp.data.source.ContactsLocalDataSource;
import com.hellogroup.connectapp.util.AppExecutors;
import com.hellogroup.connectapp.util.DiskIOThreadExecutor;

import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class ContactsRepositoryModule {

    private static final int THREAD_COUNT = 3;

    @Singleton
    @Binds
    @Device
    abstract ContactsDataSource provideContactsLocalDataSource(ContactsLocalDataSource dataSource);

//    @Singleton
//    @Binds
//    abstract ContactFetcher provideContactsFetcher(ContactFetcher contactFetcher);

    @Singleton
    @Provides
    static AppExecutors provideAppExecutors() {
        return new AppExecutors(new DiskIOThreadExecutor(),
                Executors.newFixedThreadPool(THREAD_COUNT),
                new AppExecutors.MainThreadExecutor());
    }
}
