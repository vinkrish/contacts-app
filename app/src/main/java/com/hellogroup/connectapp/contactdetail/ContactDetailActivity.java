package com.hellogroup.connectapp.contactdetail;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hellogroup.connectapp.R;
import com.hellogroup.connectapp.data.Contact;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.support.DaggerAppCompatActivity;

public class ContactDetailActivity extends DaggerAppCompatActivity implements ContactDetailContract.View {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.contact_image) ImageView contactImage;
    @BindView(R.id.contact_name) TextView contactName;
    @BindView(R.id.contact_number) TextView contactNumber;
    @BindView(R.id.number_type) TextView numberType;
    @BindView(R.id.contact_email) TextView contactEmail;
    @BindView(R.id.email_type) TextView emailType;
    @BindView(R.id.number_layout) RelativeLayout numberLayout;
    @BindView(R.id.sms_img) ImageView smsImg;
    @BindView(R.id.email_layout) RelativeLayout emailLayout;

    public static final String EXTRA_CONTACT_ID = "CONTACT_ID";
    private long contactId;

    @Inject
    ContactDetailPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if(getIntent() != null) {
            contactId = getIntent().getExtras().getLong(EXTRA_CONTACT_ID, 0);
        }

        setupClickListeners();

    }

    private void setupClickListeners() {
        numberLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mPresenter.openCaller();
            }
        });

        smsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.openMessenger();
            }
        });

        emailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.openEmailClient();
            }
        });

        numberLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mPresenter.triggerContactNumberCopy();
                return true;
            }
        });

        emailLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mPresenter.triggerContactEmailCopy();
                return true;
            }
        });
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showContact(Contact contact) {
        contactName.setText(contact.getDisplayName());
        contactNumber.setText(contact.getPhoneNumber());
        numberType.setText(contact.getPhoneType());
        contactEmail.setText(contact.getEmailAddress());
        emailType.setText(contact.getEmailType());
    }

    @Override
    public void showCaller() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + contactNumber.getText().toString()));
        startActivity(intent);
    }

    @Override
    public void showMessenger() {
        Uri sms_uri = Uri.parse("smsto:+" + contactNumber.getText().toString());
        Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
        sms_intent.putExtra("sms_body", "Hello World!");
        startActivity(sms_intent);
    }

    @Override
    public void copyContactNumber() {
        ClipboardManager cm = (ClipboardManager)this.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(contactNumber.getText().toString());
        Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void copyContactEmail() {
        final android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager)
                this.getSystemService(Context.CLIPBOARD_SERVICE);
        final android.content.ClipData clipData = android.content.ClipData
                .newPlainText("text label", contactEmail.getText().toString());
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmailClient() {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{" " + contactEmail.getText().toString() + " "});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Test");
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.takeView(this);
        mPresenter.loadContactDetails(contactId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dropView();
    }
}
