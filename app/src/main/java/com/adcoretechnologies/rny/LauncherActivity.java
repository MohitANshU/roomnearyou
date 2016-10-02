package com.adcoretechnologies.rny;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.adcoretechnologies.rny.auth.login.LoginActivity;
import com.adcoretechnologies.rny.home.seller.HomeSellerActivity;
import com.adcoretechnologies.rny.util.Const;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LauncherActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 2;

    @BindView(R.id.llParent)
    LinearLayout llParent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_launcher);

        ButterKnife.bind(this);

        askReadExternalStoragePermission();

//        launchApp();

    }

    private void askReadExternalStoragePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            askLocationPermission();
        } else {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar snackbar = Snackbar
                        .make(llParent, "You must provide permission to allow app functioning correctly", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions(LauncherActivity.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_ACCESS_EXTERNAL_STORAGE);
                            }
                        });
                snackbar.show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_ACCESS_EXTERNAL_STORAGE);

            }
        }
    }

    private void askLocationPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            launchApp();
        } else {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Snackbar snackbar = Snackbar
                        .make(llParent, "You must provide permission to allow app functioning correctly", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions(LauncherActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        });
                snackbar.show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    askLocationPermission();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(llParent, "You must provide permission to allow app to function correctly", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ActivityCompat.requestPermissions(LauncherActivity.this,
                                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                            MY_PERMISSIONS_REQUEST_ACCESS_EXTERNAL_STORAGE);
                                }
                            });
                    snackbar.show();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    launchApp();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(llParent, "You must provide permission to allow app to function correctly", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ActivityCompat.requestPermissions(LauncherActivity.this,
                                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                            MY_PERMISSIONS_REQUEST_LOCATION);
                                }
                            });
                    snackbar.show();
                }
                return;
            }
        }
    }

    private void launchApp() {
        Intent intent = new Intent(LauncherActivity.this, HomeSellerActivity.class);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            intent = new Intent(LauncherActivity.this, LoginActivity.class);
        } else {
            if (Const.IS_TEST)
                intent = new Intent(LauncherActivity.this, HomeSellerActivity.class);
        }

        startActivity(intent);
        finish();
    }
}
