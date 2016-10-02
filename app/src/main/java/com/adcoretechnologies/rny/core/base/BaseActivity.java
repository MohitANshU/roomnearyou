package com.adcoretechnologies.rny.core.base;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.adcoretechnologies.rny.R;
import com.adcoretechnologies.rny.util.Const;
import com.mikepenz.iconics.context.IconicsLayoutInflater;


public abstract class BaseActivity extends AppCompatActivity {

    ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        //  Restrict to portrait mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
//    }

    public ProgressDialog showProgressDialog(@Nullable String title, @Nullable String description) {
        dialog = new ProgressDialog(this);
        dialog.setCancelable(true);
        dialog.setTitle(title);
        dialog.setMessage(description);
        dialog.show();
        return dialog;
    }

    public void hideDialog() {
        if (dialog != null)
            dialog.dismiss();
    }

    public void showExitDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme_Dialog_Alert);
        builder.setTitle(title);
        builder.setMessage(message);

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public void toast(String message) {
        try {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            log("Window has been closed");
        }
    }

    public abstract void init();

    public abstract void log(String message);

    public void log(String className, String message) {
        Log.d(Const.DEBUG_TAG, className + " : " + message);
    }

    @Override
    public void onStop() {
        super.onStop();
        hideDialog();
    }
}

