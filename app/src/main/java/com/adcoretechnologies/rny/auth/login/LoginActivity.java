package com.adcoretechnologies.rny.auth.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.widget.TextView;

import com.adcoretechnologies.rny.R;
import com.adcoretechnologies.rny.auth.register.RegisterActivity;
import com.adcoretechnologies.rny.core.base.BaseActivity;
import com.adcoretechnologies.rny.home.seller.HomeSellerActivity;
import com.adcoretechnologies.rny.util.Const;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
            etUserName.setText("Email@gmail.com");
            etPassword.setText("123456");
        }
    }

    @OnClick(R.id.btnLogin)
    public void onLogin() {
        String username = etUserName.getText().toString();
        String password = etPassword.getText().toString();

        performLogin(username, password);
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
        startActivity(new Intent(this, HomeSellerActivity.class));
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
