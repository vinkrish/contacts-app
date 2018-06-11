package com.hellogroup.connectapp.addeditcontact;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.hellogroup.connectapp.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class AddEditContactActivity extends DaggerAppCompatActivity implements AddEditContactContract.View{
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.name) TextInputLayout nameInputLayout;
    @BindView(R.id.phone) TextInputLayout phoneInputLayout;
    @BindView(R.id.email) TextInputLayout emailInputlayout;
    @BindView(R.id.name_et) EditText mName;
    @BindView(R.id.phone_et) EditText mPhone;
    @BindView(R.id.email_et) EditText mEmail;

    public static final int REQUEST_ADD_CONTACT = 1;
    public static final String ARGUMENT_EDIT_CONTACT_ID = "EDIT_CONTACT_ID";

    @Inject
    AddEditContactContract.Presenter mPresenter;

    @Inject
    long contactId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_contact);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_contact:
                //save contact
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void setName(String name) {
        mName.setText(name);
    }

    @Override
    public void setPhone(String phone) {
        mPhone.setText(phone);
    }

    @Override
    public void setEmail(String email) {
        mEmail.setText(email);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.takeView(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dropView();
    }
}
