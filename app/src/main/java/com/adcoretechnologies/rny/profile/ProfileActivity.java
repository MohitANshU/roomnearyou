package com.adcoretechnologies.rny.profile;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adcoretechnologies.rny.R;
import com.adcoretechnologies.rny.auth.BoUser;
import com.adcoretechnologies.rny.core.base.BaseActivity;
import com.adcoretechnologies.rny.util.Common;
import com.adcoretechnologies.rny.util.Const;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends BaseActivity {


    Toolbar toolbar;
    @BindView(R.id.ivProfilePic)
    ImageView ivProfilePic;
    @BindView(R.id.llProfile)
    LinearLayout llProfile;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.etName)
    TextInputEditText etName;
    @BindView(R.id.etEmail)
    TextInputEditText etEmail;
    @BindView(R.id.etContactNumber)
    TextInputEditText etContactNumber;
    @BindView(R.id.tvLocality)
    TextView tvLocality;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }

    @Override
    public void init() {
        fillData();
    }

    private void fillData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            etName.setText(user.getDisplayName());
            etEmail.setText(user.getEmail());
//            fetchDetailProfile();
        } else {
            toast("You are not logged in");
            return;
        }
    }

    private void fetchDetailProfile() {
        userRef = FirebaseDatabase.getInstance().getReference(Const.FIREBASE_DB_USER);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BoUser user = dataSnapshot.getValue(BoUser.class);
                bindData(user);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                toast("Failed to read property detail");
                Common.logException(getApplicationContext(), "Failed to read user detail", error);
            }
        });
    }

    private void bindData(BoUser user) {
        if (user != null) {
            tvLocality.setText(user.locality);
            etContactNumber.setText(user.contactNumber);
            etContactNumber.setEnabled(false);
        }
    }

    @OnClick(R.id.fabSave)
    public void onSave() {
        toast("Under development");
    }

    @OnClick(R.id.tvProfilePic)
    public void onProfileUpdate() {
        toast("Under development");
    }

    @Override
    public void log(String message) {

    }
}
