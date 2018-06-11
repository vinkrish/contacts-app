package com.hellogroup.connectapp.addeditcontact;

import android.os.Bundle;

import com.hellogroup.connectapp.R;

import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class AddEditContactActivity extends DaggerAppCompatActivity implements AddEditContactContract.View{

    public static final int REQUEST_ADD_CONTACT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_contact);
        ButterKnife.bind(this);

    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }
}
