package com.adcoretechnologies.rny.auth.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.widget.TextView;

import com.adcoretechnologies.rny.R;
import com.adcoretechnologies.rny.auth.BoUser;
import com.adcoretechnologies.rny.auth.register.RegisterActivity;
import com.adcoretechnologies.rny.core.base.BaseActivity;
import com.adcoretechnologies.rny.home.RoleChooserActivity;
import com.adcoretechnologies.rny.util.Const;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.etUserName)
    TextInputEditText etUserName;
    @BindView(R.id.etPassword)
    TextInputEditText etPassword;
    @BindView(R.id.tvVersion)
    TextView tvVersion;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
            etUserName.setText("9874563211");
            etPassword.setText("123456");
        }
    }

    @OnClick(R.id.btnLogin)
    public void onLogin() {
        String username = etUserName.getText().toString();
        String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(username)) {
            etUserName.setError("Please provide username");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Please provide password");
            return;
        } else if (password.length() < 6) {
            etPassword.setError("Password must be greater than 6 digits");
            return;
        }
        performLogin(username, password);
//        lookupEmailId(username, password);
    }

    private void lookupEmailId(String contactNumber, final String password) {
        showProgressDialog("Performing login", "Please wait...");
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(Const.FIREBASE_DB_USER);
        Query emailQuery = userRef.orderByChild("contactNumber").equalTo(contactNumber, "contactNumber");
        emailQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BoUser user = dataSnapshot.getValue(BoUser.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void performLogin(String username, String password) {
        showProgressDialog("Performing login", "Please wait...");
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideDialog();

                        if (task.isSuccessful()) {
                            toast("Login successfully");
                            redirectToNextScreen();
                        } else {
                            toast("Invalid credential");
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

    @OnClick(R.id.tvRegister)
    public void onRegister() {
        startActivity(new Intent(this, RegisterActivity.class));
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
