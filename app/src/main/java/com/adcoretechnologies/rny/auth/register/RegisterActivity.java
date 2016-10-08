package com.adcoretechnologies.rny.auth.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.widget.RadioButton;
import android.widget.TextView;

import com.adcoretechnologies.rny.R;
import com.adcoretechnologies.rny.auth.BoUser;
import com.adcoretechnologies.rny.auth.login.LoginActivity;
import com.adcoretechnologies.rny.core.base.BaseActivity;
import com.adcoretechnologies.rny.home.RoleChooserActivity;
import com.adcoretechnologies.rny.util.Common;
import com.adcoretechnologies.rny.util.Const;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    private static final int PLACE_PICKER_REQUEST = 101;
    @BindView(R.id.etName)
    TextInputEditText etName;
    @BindView(R.id.rbMale)
    RadioButton rbMale;
    @BindView(R.id.rbFemale)
    RadioButton rbFemale;
    @BindView(R.id.etEmail)
    TextInputEditText etEmail;
    @BindView(R.id.etPassword)
    TextInputEditText etPassword;
    @BindView(R.id.etContactNumber)
    TextInputEditText etContactNumber;
    @BindView(R.id.tvLocality)
    TextView tvLocality;

    private double latitude;
    private double longitude;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        init();
    }

    @Override
    public void init() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    log("onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    log("onAuthStateChanged:signed_out");
                }
            }
        };

        if (Const.IS_TEST) {
            etName.setText("Test name");
            rbMale.setChecked(true);
            etEmail.setText("Email@gmail.com");
            etPassword.setText("123456");
            etContactNumber.setText("9876543219");
            tvLocality.setText("South ex, Delhi");
            latitude = 28.432432;
            longitude = 123.324324;
        }
    }

    @OnClick(R.id.btnRegister)
    public void onRegister() {
        String defaultError = "Please provide input";

        String name = etName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            etName.setError(defaultError);
            return;
        }
        String contactNumber = etContactNumber.getText().toString();
        if (TextUtils.isEmpty(contactNumber)) {
            etContactNumber.setError(defaultError);
            return;
        } else if (contactNumber.length() != 10) {
            etContactNumber.setError("Contact number must be of 10 digit");
            return;
        }

        String email = etEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            etEmail.setError(defaultError);
            return;
        }

        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(name)) {
            etPassword.setError(defaultError);
            return;
        } else if (password.length() < 6) {
            etPassword.setError("Password must be of 6 digits");
            return;
        }


        String locality = tvLocality.getText().toString();
        if (TextUtils.isEmpty(locality) || latitude == 0.0 || longitude == 0.0) {
            toast("Please select your locality");
            return;
        }

        String gender = "M";
        if (rbFemale.isChecked())
            gender = "F";
        String imei = Common.getImei(getApplicationContext());

        performRegistration(name, email, password, contactNumber, locality, gender, imei, latitude, longitude);
    }

    private void performRegistration(final String name, final String email, String password, final String contactNumber, final String locality, final String gender, final String imei, final double latitude, final double longitude) {

        showProgressDialog("Performing registration", "Please wait...");
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                DatabaseReference userRef = database.getReference(Const.FIREBASE_DB_USER).child(user.getUid());
                                                BoUser boUser = new BoUser(name, gender, email, contactNumber, locality, latitude, longitude, imei);
                                                userRef.setValue(boUser, new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                        hideDialog();

                                                        if (databaseError == null) {
                                                            toast("Registration successful");
                                                            redirectToNextScreen();
                                                        } else {
                                                            toast("Failed creating user profile. Please try again");
                                                            Common.logException(getApplicationContext(), "Registration error", databaseError);
                                                        }
                                                    }
                                                });
                                            } else {
                                                toast("Problem with registration. Please try again");
                                                hideDialog();
                                            }
                                        }
                                    });


                        } else {
                            toast("Problem with registration. Please try again");
                            hideDialog();
                        }
                    }
                });
    }

    private void redirectToNextScreen() {
        Intent intent = new Intent(this, RoleChooserActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.tvLocality)
    public void openPlacePicker() {
        toast("Please wait...");
        PlacePicker.IntentBuilder picker = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(picker.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            toast("You need to update google play service in order to use this feature");
            updateStatus("You need to update google play service in order to use this feature");
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            toast("This feature requires Google play service to work.");
            updateStatus("This feature requires Google play service to work.");
            e.printStackTrace();
        }
    }

    private void updateStatus(String message) {
        toast(message);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case PLACE_PICKER_REQUEST: {
                if (resultCode == Activity.RESULT_OK) {
                    Place place = PlacePicker.getPlace(imageReturnedIntent, this);
                    updatePlace(place);
                } else
                    updateStatus("You have not selected any place");
            }
        }
    }

    public void updatePlace(Place place) {
        if (place != null) {
            tvLocality.setText(place.getAddress());
            latitude = place.getLatLng().latitude;
            longitude = place.getLatLng().longitude;

        }
    }

    @OnClick(R.id.tvLogin)
    public void onLogin() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void log(String message) {
        super.log(getClass().getSimpleName(), message);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
