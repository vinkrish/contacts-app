package com.hellogroup.connectapp.contactdetail;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.hellogroup.connectapp.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.support.DaggerAppCompatActivity;

public class ContactDetailActivity extends DaggerAppCompatActivity implements ContactDetailContract.View {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;

    public static final String EXTRA_CONTACT_ID = "CONTACT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        //.getBackground().setAlpha(0);
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void onResume() {
        super.onResume();
        //mPresenter.takeView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mPresenter.dropView();
    }
}
