package com.adcoretechnologies.rny.core.base;

import android.app.ProgressDialog;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.adcoretechnologies.rny.util.Const;

/**
 * Created by Irfan on 05/06/16.
 */
public abstract class BaseFragment extends Fragment {

    private ProgressDialog dialog;

    public void toast(String message) {
        try {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            log("Window has been closed");
        }
    }

    public ProgressDialog showProgressDialog(@Nullable String title, @Nullable String description) {
        dialog = new ProgressDialog(getActivity());
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

    public abstract void init();

    public abstract void log(String message);

    public void log(String className, String message) {
        Log.d(Const.DEBUG_TAG, className + " : " + message);
    }
}
