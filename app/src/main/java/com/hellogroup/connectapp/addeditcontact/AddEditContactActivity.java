package com.hellogroup.connectapp.addeditcontact;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.hellogroup.connectapp.R;
import com.hellogroup.connectapp.data.Contact;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class AddEditContactActivity extends DaggerAppCompatActivity implements
        AddEditContactContract.View{
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.name) TextInputLayout nameInputLayout;
    @BindView(R.id.phone) TextInputLayout phoneInputLayout;
    @BindView(R.id.email) TextInputLayout emailInputlayout;
    @BindView(R.id.name_et) EditText mName;
    @BindView(R.id.phone_et) EditText mPhone;
    @BindView(R.id.email_et) EditText mEmail;

    public static final int REQUEST_ADD_CONTACT = 2;
    public static final String ARGUMENT_EDIT_CONTACT_ID = "EDIT_CONTACT_ID";
    private String mRawPhoneNumber;

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
                Contact contact = new Contact();
                contact.setRawPhoneNumber(mRawPhoneNumber);
                contact.setDisplayName(mName.getText().toString());
                contact.setPhoneNumber(mPhone.getText().toString());
                contact.setPhoneType("Work");
                contact.setEmailAddress(mEmail.getText().toString());
                contact.setEmailType("Work");
                mPresenter.saveContact(contact);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void setName(String name, String rawPhoneNumber) {
        mName.setText(name);
        mRawPhoneNumber = rawPhoneNumber;
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
    public void showEmptyNameError() {
        mName.setError("Name can't be empty!");
    }

    @Override
    public void showPhoneError(String error) {
        mPhone.setError(error);
    }

    @Override
    public void showEmailError(String error) {
        mEmail.setError(error);
    }

    @Override
    public void showContactList() {
        setResult(Activity.RESULT_OK);
        finish();
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
