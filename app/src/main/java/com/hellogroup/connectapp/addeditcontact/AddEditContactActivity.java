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
    @BindView(R.id.name_et) EditText nameEditText;
    @BindView(R.id.phone_et) EditText phoneEditText;
    @BindView(R.id.email_et) EditText emailEditText;

    public static final int REQUEST_ADD_CONTACT = 1;
    public static final String ARGUMENT_EDIT_CONTACT_ID = "EDIT_CONTACT_ID";

    @Inject
    AddEditContactPresenter mPresenter;

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
        // Handle item selection
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
