package com.hellogroup.connectapp.contactdetail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hellogroup.connectapp.R;

import dagger.android.AndroidInjection;
import dagger.android.support.DaggerAppCompatActivity;

public class ContactDetailActivity extends DaggerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
    }
}
