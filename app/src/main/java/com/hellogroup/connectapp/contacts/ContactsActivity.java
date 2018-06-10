package com.hellogroup.connectapp.contacts;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.hellogroup.connectapp.R;
import com.hellogroup.connectapp.contactdetail.ContactDetailActivity;
import com.hellogroup.connectapp.data.Contact;
import com.hellogroup.connectapp.data.ContactsQuery;
import com.hellogroup.connectapp.util.library.PinnedHeaderListView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class ContactsActivity extends DaggerAppCompatActivity implements ContactsContract.View {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(android.R.id.list) PinnedHeaderListView mListView;

    private ContactsAdapter mAdapter;
    private LayoutInflater mInflater;

    @Inject
    ContactsPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        setupListView();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setupListView() {
        mInflater = LayoutInflater.from(ContactsActivity.this);
        mAdapter = new ContactsAdapter(this, new ArrayList<Contact>(0), mItemListener);

        int pinnedHeaderBackgroundColor = getResources().getColor(getResIdFromAttribute(this, android.R.attr.colorBackground));
        mAdapter.setPinnedHeaderBackgroundColor(pinnedHeaderBackgroundColor);
        mAdapter.setPinnedHeaderTextColor(getResources().getColor(R.color.pinned_header_text));
        mListView.setPinnedHeaderView(mInflater.inflate(R.layout.pinned_header_listview_side_header, mListView, false));
        mListView.setAdapter(mAdapter);
        mListView.setOnScrollListener(mAdapter);
        mListView.setEnableHeaderTransparencyChanges(false);
    }

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
        mAdapter.replaceData(contactList);
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

    public interface ContactItemListener {
        void onContactClick(Contact clickedContact);
    }

    ContactItemListener mItemListener = new ContactItemListener() {
        @Override
        public void onContactClick(Contact clickedContact) {
            Log.d("_id", clickedContact.contactId + " ");
            Log.d("name", clickedContact.displayName);
            mPresenter.openContactDetails(clickedContact);
        }
    };

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
