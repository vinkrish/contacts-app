package com.hellogroup.connectapp.contacts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.hellogroup.connectapp.R;
import com.hellogroup.connectapp.addeditcontact.AddEditContactActivity;
import com.hellogroup.connectapp.contactdetail.ContactDetailActivity;
import com.hellogroup.connectapp.data.Contact;
import com.hellogroup.connectapp.util.PermissionUtil;
import com.hellogroup.connectapp.util.library.PinnedHeaderListView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class ContactsActivity extends DaggerAppCompatActivity implements
        ContactsContract.View, ActivityCompat.OnRequestPermissionsResultCallback {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(android.R.id.list) PinnedHeaderListView mListView;
    //@BindView(R.id.recycler_view) RecyclerView recyclerView;

    private ContactsAdapter mAdapter;
    private ContactsRecyclerAdapter recyclerAdapter;
    private LayoutInflater mInflater;

    private static final int READ_CONTACTS_PERMISSION = 999;

    @Inject
    ContactsPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        setupListView(new ArrayList<Contact>(0));
        //setupRecyclerView();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.addNewContact();
            }
        });

        PermissionUtil.isContactsReadingPermissionGranted(this, READ_CONTACTS_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mPresenter.takeView(this);
        } else {
            Snackbar.make(coordinatorLayout, "Reading Contacts Permission has been denied!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private void setupListView(ArrayList<Contact> contacts) {
        //mInflater = LayoutInflater.from(ContactsActivity.this);
        mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mAdapter = new ContactsAdapter(this, contacts, mItemListener);

        int pinnedHeaderBackgroundColor = getResources().getColor(getResIdFromAttribute(this, android.R.attr.colorBackground));
        mAdapter.setPinnedHeaderBackgroundColor(pinnedHeaderBackgroundColor);
        mAdapter.setPinnedHeaderTextColor(getResources().getColor(R.color.pinned_header_text));
        mListView.setPinnedHeaderView(mInflater.inflate(R.layout.pinned_header_listview_side_header, mListView, false));
        mListView.setAdapter(mAdapter);
        mListView.setOnScrollListener(mAdapter);
        mListView.setEnableHeaderTransparencyChanges(false);
    }

    /*private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerAdapter = new ContactsRecyclerAdapter(new ArrayList<Contact>(0), mItemListener);
        recyclerView.setAdapter(recyclerAdapter);
    }*/

    public static int getResIdFromAttribute(final Activity activity, final int attr) {
        if (attr == 0)
            return 0;
        final TypedValue typedValue = new TypedValue();
        activity.getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.resourceId;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if(active) progressBar.setVisibility(View.VISIBLE);
        else progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showContacts(ArrayList<Contact> contactList) {
        mAdapter.setData(contactList);
        //recyclerAdapter.setDataSet(contactList);
    }

    @Override
    public void showNoContacts() {
        Snackbar.make(coordinatorLayout, "Show no contacts...", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void showContactDetailsUi(long contactId) {
        Intent intent = new Intent(getApplicationContext(), ContactDetailActivity.class);
        intent.putExtra(ContactDetailActivity.EXTRA_CONTACT_ID, contactId);
        startActivity(intent);
    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage("New Contact Saved");
        mPresenter.loadContacts();
    }

    private void showMessage(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showAddContact() {
        Intent intent = new Intent(getApplicationContext(), AddEditContactActivity.class);
        startActivityForResult(intent, AddEditContactActivity.REQUEST_ADD_CONTACT);
    }

    public interface ContactItemListener {
        void onContactClick(Contact clickedContact);
    }

    ContactItemListener mItemListener = new ContactItemListener() {
        @Override
        public void onContactClick(Contact clickedContact) {
            mPresenter.openContactDetails(clickedContact);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(PermissionUtil.getContactsReadingPermissionStatus(this)) {
            mPresenter.takeView(this);
        }
        /*if(recyclerAdapter.getItemCount()==0 && PermissionUtil.getContactsReadingPermissionStatus(this)) {
            mPresenter.takeView(this);
        }*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dropView();
    }

}
